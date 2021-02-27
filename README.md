# Crawler
## What it does
This project is to make a webcrawler that fetches urls, parses links with regular expressions and outputs to the terminal.  It uses java 11 and maven version 4.
External libraries used are junit4 for testing, org.apache.log4j2 for logging multithreaded code. 

## Build Instructions
First run
```
mvn clean package
```
Then to test manually:
```
java -cp target/Crawler-1.0-SNAPSHOT-jar-with-dependencies.jar crawler.Driver [ url ]
```