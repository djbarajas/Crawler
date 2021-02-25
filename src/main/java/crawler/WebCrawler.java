package crawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for web crawling.  It fetches urls, parses for links and prints
 *
 * @author Daniel Barajas
 * @version Spring 2021
 */
public class WebCrawler {

    /** the logger to debug */
    private final Logger logger = LogManager.getLogger();
    /** the urls that have been visited */
    private final Map<String, Integer> visited_urls;
    /** the work queue to multithread */
    private final ExecutorService executor;

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
        this.visited_urls = new ConcurrentHashMap<>();
        this.executor = Executors.newFixedThreadPool(5);
        this.limit = 10;
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
        this.visited_urls.put(url, 1);
        Set<String> links;

        while (!queue.isEmpty()) {
            String link = queue.remove();
            try {
                links = WebCrawlerUtil.getLinks(link);
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
                if (this.visited_urls.size() < this.limit && !this.visited_urls.containsKey(neighbor)) {
                    this.visited_urls.put(neighbor, 1);
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

        if (this.limit < Integer.MAX_VALUE) {
            shutdownExecutor();
        }
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

        /** the url to fetch */
        private final String url;

        /**
         * Task for a certain url to fetch child links
         *
         * @param url
         */
        public Task(String url) {
            this.url = url;
        }

        @Override
        public void run() {
//            try {
//
//            } catch (IOException e) {
//                logger.debug("Task for " + this.url + " failed.");
//            }
            logger.debug("Task for " + this.url + " completed.");
        }
    }
}
