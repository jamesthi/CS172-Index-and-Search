package edu.ucr.qlyu001.Lucene;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Iterator;

import org.apache.lucene.document.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class App {
	static class Page {
		String title;
		String latitude;
		String longitude;
		String source;
		String Date;
		String tweet_urls;
		String Hashtags;
		String text;

		Page(String title, String latitude, String longitude, String source, String Date, String Hashtags, String text,
				String tweet_urls) {
			this.title = title;
			this.latitude = latitude;
			this.longitude = longitude;
			this.source = source;
			this.Date = Date;
			this.tweet_urls = tweet_urls;
			this.Hashtags = Hashtags;
			this.text = text;
		}
	}

	public static Document getDoc(Page tweet) throws IOException {

		Document doc = new Document();
		doc.add(new Field("title", tweet.title, TextField.TYPE_STORED));
		doc.add(new Field("latitude", tweet.latitude, TextField.TYPE_STORED));
		doc.add(new Field("longitude", tweet.longitude, TextField.TYPE_STORED));
		doc.add(new Field("source", tweet.source, TextField.TYPE_STORED));
		doc.add(new Field("Date", tweet.Date, TextField.TYPE_STORED));
		doc.add(new Field("tweet_urls", tweet.tweet_urls, TextField.TYPE_STORED));
		doc.add(new Field("Hashtags", tweet.Hashtags, TextField.TYPE_STORED));
		doc.add(new Field("text", tweet.text, TextField.TYPE_STORED));
		return doc;
	}
	
	public boolean isInteger( String input ) {
	    try {
	        Integer.parseInt( input );
	        return true;
	    }
	    catch( Exception e ) {
	        return false;
	    }
	}

	public String rankQueryHTML(String Q, String numQ) throws IOException, ParseException {
		Directory directory = FSDirectory
				.open(FileSystems.getDefault().getPath("C:\\UCR\\Spring 2018\\CS172\\Project\\index", "index"));
		String query_string = Q; // what the actual query is
		int topHitCount;

		DirectoryReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		Analyzer analyzer = new StandardAnalyzer();
		String[] fields = { "title", "Hashtags", "text" }; // what fields to search for
		
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);

		String output = "<b>SEARCHING FOR: " + query_string + "</b> <br/> <br/>";
		
		if (Q.trim().length() <= 2) {
			output += "Please enter a valid query of 3 or more letters.";
		}
		else {
			if (!isInteger(numQ)) {
				output += "Invalid input for number of results; defaulted to max 10 results. <br/> <br/>";
				topHitCount = 10;
			}
			else if (Integer.parseInt(numQ) <= 0) {
				output += "No valid input for number of results; defaulted to max 10 results. <br/> <br/>";
				topHitCount = 10;
			}
			else {
				topHitCount = Integer.parseInt(numQ);
			}
			
			// System.out.println("Searching for: " + query_string);
			Query query = parser.parse(query_string);
			ScoreDoc[] hits = indexSearcher.search(query, topHitCount).scoreDocs;
			
			output = output + "<b>Results shown:</b> " + hits.length + "<br/> <br/>";
			
			for (int rank = 0; rank < hits.length; ++rank) {
				Document hitDoc = indexSearcher.doc(hits[rank].doc);

				output = output + "<b>" + (rank + 1) + " (score:" + hits[rank].score + ") --> Tweet: </b>"
						+ hitDoc.get("text") + " - <b>Date:</b> " + hitDoc.get("Date");
				
				if(hitDoc.get("title").length() > 4)
					output = output + " - <b>Linked Tweet:</b> " + hitDoc.get("title");
				
				output = output + " - <b>Tweet Source:</b> " + hitDoc.get("source") + " <br/> <br/>";

			}
		}

		indexReader.close();
		directory.close();

		return output;
	}

	public static void main(String[] args) throws IOException, ParseException {
		// If you need to index, toindex = 1
		// If you already have an index directory, toindex = 0
		
		int toindex = 3; // main function unused since output is redirected to the Web UI. 
						 // For testing and indexing toindex is set to 1 or 2.

		if (toindex == 1) { /**** Index function begins here ****/
			Directory directory = FSDirectory
					.open(FileSystems.getDefault().getPath("C:\\UCR\\Spring 2018\\CS172\\Project\\index", "index"));
			InputStream stopWords = new FileInputStream("C:\\UCR\\Spring 2018\\CS172\\Project\\stopwords.txt");
			Reader readerStopWords = new InputStreamReader(stopWords);
			StandardAnalyzer analyzer = new StandardAnalyzer(readerStopWords);
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter indexWriter = new IndexWriter(directory, config);

			int filenumber = 0;

			String line = null;
			JSONObject obj;

			File file = new File("C:\\UCR\\Spring 2018\\CS172\\Project\\Datafiles\\" + filenumber + ".json");
			String fileName = "C:\\UCR\\Spring 2018\\CS172\\Project\\Datafiles\\" + filenumber + ".json";

			try {

				while (file.exists()) {
					
					FileReader fileReader = new FileReader(fileName);

					// Always wrap FileReader in BufferedReader.
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					line = bufferedReader.readLine();
					while ((line = bufferedReader.readLine()) != null) {
						obj = (JSONObject) new JSONParser().parse(line);
					
						String title = (String) obj.get("url_title").toString();

						String latitude = obj.get("latitude").toString();

						String longitude = obj.get("longitude").toString();

						String source = (String) obj.get("source");
						String Date = (String) obj.get("Date");
						Object tweet_urlsO = obj.get("tweet_urls");

						String tweet_urls = "";
						if (tweet_urlsO instanceof JSONArray) {
							// It's an array
							JSONArray tweet_urlsA = (JSONArray) obj.get("tweet_urls");
							Iterator<String> iterator1 = tweet_urlsA.iterator();
							while (iterator1.hasNext()) {
								tweet_urls += iterator1.next();

							}
						} else {
							// It's an object
							tweet_urls = (String) obj.get("tweet_urls");
						}

						JSONArray HashtagsO = (JSONArray) obj.get("Hashtags");
						String Hashtags = "";
						Iterator<String> iterator2 = HashtagsO.iterator();
						while (iterator2.hasNext()) {
							Hashtags += iterator2.next();

						}
						String text = (String) obj.get("text");
						Page tweet1 = new Page(title, latitude, longitude, source, Date, Hashtags, text, tweet_urls);
						indexWriter.addDocument(getDoc(tweet1));
					}
					// Always close files.
					bufferedReader.close();
					System.out.println("Finished indexing: " + filenumber + ".json");
					filenumber++;
					file = new File("C:\\UCR\\Spring 2018\\CS172\\Project\\Datafiles\\" + filenumber + ".json");
					fileName = "C:\\UCR\\Spring 2018\\CS172\\Project\\Datafiles\\" + filenumber + ".json";
				}

			} catch (Throwable e) {
				e.printStackTrace();
			}

			indexWriter.close();

		} 	/**** End Index Function ****/

		
		/**** Rank Function begins here ****/
		else if (toindex == 0) { 
			// address of the index folder
			Directory directory = FSDirectory
					.open(FileSystems.getDefault().getPath("C:\\UCR\\Spring 2018\\CS172\\Project\\index", "index"));
			String query_string = "testing"; // what the actual query is
			int topHitCount = 100; // how many hits you want

			DirectoryReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			Analyzer analyzer = new StandardAnalyzer();
			String[] fields = { "title", "Hashtags", "text" }; // what fields to search for
																
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
			System.out.println("Searching for: " + query_string);
			Query query = parser.parse(query_string);
			ScoreDoc[] hits = indexSearcher.search(query, topHitCount).scoreDocs;

			for (int rank = 0; rank < hits.length; ++rank) {
				Document hitDoc = indexSearcher.doc(hits[rank].doc);
				System.out.println((rank + 1) + " (score:" + hits[rank].score + ") --> Tweet: " + hitDoc.get("text")
						+ " - Date Tweeted: " + hitDoc.get("Date") + " - Linked Tweet Title: " + hitDoc.get("title")
						+ " - Tweet Source: " + hitDoc.get("source"));
			}
			indexReader.close();
			directory.close();
		} /**** End Rank Function ****/

		// System.out.println(rankQuery("meow"));
	}
}
