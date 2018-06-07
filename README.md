# CS 172: Introduction to Information Retrieval | Spring 2018

## Build a Web or Social Network Search Engine

You must work in teams of three. If you cannot find a partner, email the TA to connect you to other students who are looking for a partner.

Each project report must have a section called "Collaboration Details" where you should clearly specify the contributions of each member of the team.

You have a choice of building a search engine for the Web or for Twitter.

## Part A: Build a Web Crawler for edu pages or a Twitter geotagged data collector

Web:

Your application should read a file of seed .edu URLs and crawl the .edu pages.

The application should also input the number of pages to crawl and the number of levels (hops (i.e. hyperlinks) away from the seed URLs).

All crawled pages (html files) should be stored in a folder.

We recommend using Java, which is the language that we will use in the discussion sections. If you use another language, you cannot expect to get any support from the TA if you get stuck. You should not use any crawler package, since the purpose of the project is to see some of the challenges involved in building a crawler.

You will be graded on the correctness and efficiency of your crawler (e.g., how does it handle duplicate pages? Or is the crawler multi-threaded?).

Twitter:

Use the Twitter Streaming API to collect geolocated tweets. Store tweets in large files of about 10 MB, one tweet per row.

If a tweet contains a URL to an html page, get title of that page, and add title as an additional field of the tweet, that is, include it in the JSON of the tweet, so it becomes searchable in Part B. 
Hint: Retrieving title in real time may slow down your Twitter stream and lead to lost data.


In both cases, you should collect at least 5 GB of data.

Deliverables:

1: Report (4-5 pages) in pdf that includes:

1. Collaboration Details: Description of contribution of each team member.

2. Overview of system, including (but not limited to)

     a. Architecture.

     b. The Crawling or data collection strategy.

     c. Data Structures employed.

3. Limitations (if any) of system.

4. Instruction on how to deploy the system. Ideally, you should include a crawler.bat (Windows) or crawler.sh (Unix/Linux) executable file that takes as input all necessary parameters.

Example: [user@server]./crawler.sh <seed-File:seed.txt> <num-pages: 10000> <hops-away: 6> <output-dir>
Similarly for Twitter.

5. Screenshots showing the system in action.

2: Zip file with your code

Turn in your report and zip file to iLearn by 5/5.

## Part B: Build index and Web-based search interface

B.1: Build index using Lucene

Web:

Write a program that uses the Lucene libraries to index all the html files in the folder you created in Part A. Handle different fields like title, body, creation date (if available).

Twitter:

Write a program that parses the JSON objects of your big files form Part A and inserts them into Lucene. Handle the fields like username, location, and so on.

Again, we recommend Java.

 B2. Create a Web-based interface

The interface should contain a textbox, and a search button. When you click search, you should see a list of results (e.g., first 10) returned by Lucene for this query and their scores. The list should be ordered in decreasing order of score. Handle fields as you deem appropriate. For Twitter, order by a combination of time and relevance; describe your ranking function.

You can use the web development language of your choice. We will partially cover Angular in the discussion sections.

Do not use SOLR or another framework that automatically builds the UI for you.

Extra credit:

Web: Display a snippet for each result. You can use ideas discussed in class or your own ideas to come up with a good snippet generation algorithm. Don't use Lucene-generated snippets.

Alternatively, you can use PageRank to rank pages, that is, you can allow the user to either rank by Lucene ranking or by PageRank.

Twitter: Display results on map. Read user's location from browser if available and center map there. Only show tweets within 100 miles from user location.

Alternatively, you can use PageRank to rank tweets, that is, you can allow the user to either rank by Lucene ranking or by PageRank.

Deliverables:

1. Collaboration Details: Description of contribution of each team member.

2. Overview of system, including (but not limited to):

         a. Architecture

         b. Index Structures

         c. Search Algorithm

3. Limitations of system.

4. Instructions on how to deploy the system. Ideally, you should include an indexer.bat (Windows) or indexer.sh (Unix/Linux) executable file that takes as input all necessary parameters 
Example: [user@server] ./indexer.sh <output-dir>

5. A web-application (e.g. Web Archive) that can be deployed to a webserver like Tomcat.

6. Screenshots showing the system in action.

Turnin your report and zip file to iLearn by 6/8. Further, each team will demonstrate their project for 5 minutes to the TA during the last discussion section. If a team cannot demonstrate at discussion section due to time constraints, you can meet with the TA right after the discussion section to demo.
