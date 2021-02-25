package crawler;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for testing web crawling.
 *
 * @author Daniel Barajas
 * @version Spring 2021
 */
public class WebCrawlerTest {

    @Test
    public void timingTest() {
        List<Long> singleThreaded = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            WebCrawler crawler = new WebCrawler(15);
            long startTime = System.currentTimeMillis();
            crawler.crawl("https://www.rescale.com/");
            long totalTime = System.currentTimeMillis() - startTime;
            singleThreaded.add(totalTime);
        }
        System.out.println(singleThreaded.toString());
        Assert.assertTrue(singleThreaded.size() > 0);
    }
}
