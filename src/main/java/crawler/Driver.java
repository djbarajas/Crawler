package crawler;

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
        if (args.length < 1) {
            System.out.println("Please input a link to crawl");
            return;
        }
        WebCrawler crawler = new WebCrawler();
        crawler.threadedCrawl(args[0]);
    }
}
