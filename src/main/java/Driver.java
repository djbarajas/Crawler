import java.io.IOException;

public class Driver {

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        try {
            crawler.crawl("https://www.rescale.com/");
        }
        catch (IOException e) {
            System.out.println("Unable to parse link");
        }
    }
}
