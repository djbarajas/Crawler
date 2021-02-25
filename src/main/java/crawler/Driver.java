package crawler;

public class Driver {

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        crawler.crawl("https://www.rescale.com/");
    }
}
