package crawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;
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

    /** the logger to debug */
    private final Logger logger = LogManager.getLogger();
    /** the urls that have been visited */
    private final Set<String> visited_urls;
    /** the work queue to multithread */
    private final ExecutorService executor;
    /** string pattern, the pattern to find in the string */
    public static final String TEXT_PATTERN = "(?s)<a[^>]*?href[^>]*?=[^>]*?\"(https?.+?)\".*?>";
    /** the compiled patter for regular expression finding */
    public static final Pattern TEXT_REGEX = Pattern.compile(TEXT_PATTERN);

    /** optional if the user wants to end the crawling early */
    private int limit;

    /**
     * Constructor with a limit to set
     *
     * @param limit the amount to crawl the webpage
     */
    public WebCrawler(int limit) {
        this();
        if (limit == -1) {
            this.limit = Integer.MAX_VALUE;
        }
        else {
            this.limit = limit;
        }
    }

    /**
     * Constructor
     */
    public WebCrawler() {
        this.visited_urls = new HashSet<>();
        this.executor = Executors.newFixedThreadPool(5);
        this.limit = 10;
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
    public void crawl(String url) {

        Queue<String> queue = new LinkedList<>();
        queue.add(url);
        this.visited_urls.add(url);
        Set<String> links;

        while (!queue.isEmpty()) {
            String link = queue.remove();
            try {
                links = getLinks(link);
            } catch (IOException e) {
                logger.debug("unable to parse link " + link);
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(link + System.lineSeparator() + "\t");
            builder.append(String.join(System.lineSeparator() + "\t", links));
            String output = builder.toString();
            System.out.println(output);
            for (String neighbor : links) {
                if (this.visited_urls.size() < this.limit && this.visited_urls.add(neighbor)) {
                    queue.add(neighbor);
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
