package crawler;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

/**
 * Class responsible for testing web crawling.
 *
 * @author Daniel Barajas
 * @version Spring 2021
 */
public class WebCrawlerTest {

    /**
     * This test checks if the link example.com contains the link specified.
     * Checks the fetch html page and the parsing with regular expressions
     *
     * @throws IOException if the fetch failed
     */
    @Test
    public void testSetContains() throws IOException {
        Set<String> urls = WebCrawlerUtil.getLinks("http://example.com/");
        Assert.assertTrue(urls.contains("https://www.iana.org/domains/example"));
    }
}
