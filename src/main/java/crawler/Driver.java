package crawler;

import java.net.URL;

/**
 * Class responsible for testing web crawler.
 *
 * @author Daniel Barajas
 * @version Spring 2021
 */
public class Driver {

    /**
     * Main class to test the program
     *
     * @param args the link to crawl
     */
    public static void main(String[] args) {
        if (args.length > 0 && WebCrawlerUtil.isValidUrl(args[0])) {
            WebCrawler crawler = new WebCrawler();
            crawler.threadedCrawl(args[0]);
        }
        else {
            System.out.println("Please input a valid link to crawl");
        }
    }
}
