package crawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;


/**
 * Class responsible for web crawling.
 *
 * @author Daniel Barajas
 * @version Spring 2021
 */
public class WebCrawler {

    /** the logger to debug */
    private final Logger logger = LogManager.getLogger(this.getClass());
    /** the urls that have been visited */
    private final Map<String, Integer> visited_urls;
    /** the work queue to multithread */
    private final ExecutorService executor;

    /**
     * Constructor
     */
    public WebCrawler() {
        this.visited_urls = new ConcurrentHashMap<>();
        this.executor = Executors.newFixedThreadPool(5);
    }

    /**
     * single threaded crawl that utilizes the bfs graph algorithm to
     * traverse links
     *
     * @param url the url to crawl
     */
    public void crawl(String url) {
        Queue<String> queue = new LinkedList<>();
        queue.add(url);
        this.visited_urls.put(url, 1);
        Set<String> urls;

        while (!queue.isEmpty()) {
            String link = queue.remove();
            try {
                urls = WebCrawlerUtil.getLinks(link);
            } catch (IOException e) {
                logger.debug("unable to parse link " + link);
                continue;
            }
            System.out.println(WebCrawlerUtil.formatOutput(link, urls));
            for (String neighbor : urls) {
                if (!this.visited_urls.containsKey(neighbor)) {
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
    }

    /**
     * Task class to multithread crawling
     */
    private class Task implements Runnable {

        /** the url to fetch */
        private final String url;

        /**
         * Task for a certain url to fetch child links
         *
         * @param url the url to fetch
         */
        public Task(String url) {
            this.url = url;
            logger.debug("Created task for " + url);
        }

        @Override
        public void run() {
            try {
                Set<String> urls = WebCrawlerUtil.getLinks(this.url);
                System.out.println(WebCrawlerUtil.formatOutput(this.url, urls));
                for (String neighbor : urls) {
                    if (!visited_urls.containsKey(neighbor)) {
                        visited_urls.put(neighbor, 1);
                        executor.submit(new Task(neighbor));
                    }
                }
            } catch (IOException e) {
                logger.debug("Task for " + this.url + " failed.");
            }
            logger.debug("Task for " + this.url + " completed.");
        }
    }
}
