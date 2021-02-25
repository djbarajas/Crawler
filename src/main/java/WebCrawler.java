import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for multithreading web crawling.  It fetches urls, parses for links and prints
 *
 * @author Daniel Barajas
 * @version Spring 2021
 */
public class WebCrawler {

    private final Logger logger = LogManager.getLogger();
    /** the urls that have been visited */
    private final Set<String> visited_urls;

    /** the work queue to multithread */
    private final ExecutorService executor;

    /** string pattern, the pattern to find in the string */
    public static final String TEXT_PATTERN = "(?s)<a[^>]*?href[^>]*?=[^>]*?\"(https?.+?)\".*?>";

    /** the compiled patter for regular expression finding */
    public static final Pattern TEXT_REGEX = Pattern.compile(TEXT_PATTERN);

    /**
     * Constructor
     */
    public WebCrawler() {
        this.visited_urls = new HashSet<>();
        this.executor = Executors.newFixedThreadPool(5);
    }

    /**
     * fetches an html page from a given url
     *
     * @param stringUrl the url to connect to
     * @return the content of the page
     * @throws IOException if the connection fails
     */
    public static String fetchContent(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        Scanner scanner = new Scanner(url.openStream());
        StringBuilder builder = new StringBuilder();
        while(scanner.hasNext()) {
            builder.append(scanner.next() + System.lineSeparator());
        }
        return builder.toString();
    }

    /**
     * calls fetch content to get the web page then parses with regular expressions
     *
     * @param url the link to fetch page
     * @return a set of links on the page
     * @throws IOException if the connection fails
     */
    public static Set<String> getLinks(String url) throws IOException {
        String content = WebCrawler.fetchContent(url);
        Set<String> links = new HashSet<>();
        Matcher matcher = TEXT_REGEX.matcher(content);
        while (matcher.find()) {
            links.add(matcher.group(1));
        }
        return links;
    }

    /**
     * single threaded crawl
     *
     * @param url the url to crawl
     * @throws IOException if a url can't be parsed
     */
    public void crawl(String url) throws IOException {
        Set<String> links = getLinks(url);
        StringBuilder builder = new StringBuilder();
        builder.append(url + System.lineSeparator() + "\t");
        builder.append(String.join(System.lineSeparator() + "\t", links));
        String output = builder.toString();
        System.out.println(output);
        for (String link : links) {
            if (this.visited_urls.add(link)) {
                try {
                    crawl(link);
                } catch (IOException e) {
                    continue;
                }
            }
        }
    }

    /**
     * threaded version of web crawler
     *
     * @param url the starting url to parse
     */
    public void threadedCrawl(String url) {
        this.executor.submit(new Task(url));
        shutdownExecutor();
    }

    /**
     * Method to shutdown the thread executor
     */
    public void shutdownExecutor() {
        logger.debug("Shutting down executor.");
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            logger.error("Unable to shutdown executor!");
        }
    }

    private class Task implements Runnable {

        private final String url;

        public Task(String url) {
            this.url = url;
        }

        @Override
        public void run() {

        }
    }
}
