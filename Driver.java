import java.io.IOException;

public class Driver {

    public static void main(String[] args) {
        try {
            System.out.println(WebCrawler.getLinks("https://www.rescale.com/").toString());
        }
        catch (IOException e) {
            System.out.println("Unable to parse link");
        }
    }
}
