package crawler;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for fetching urls, parsing for links and prints
 *
 * @author Daniel Barajas
 * @version Spring 2021
 */
public class WebCrawlerUtil {

    /** string pattern, the pattern to find in the string */
    public static final String TEXT_PATTERN = "(?s)<a[^>]*?href[^>]*?=[^>]*?\"(https?.+?)\".*?>";
    /** the compiled patter for regular expression finding */
    public static final Pattern TEXT_REGEX = Pattern.compile(TEXT_PATTERN);

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
            builder.append(scanner.next());
            builder.append(System.lineSeparator());
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
        String content = fetchContent(url);
        Set<String> urls = new HashSet<>();
        Matcher matcher = TEXT_REGEX.matcher(content);
        while (matcher.find()) {
            urls.add(matcher.group(1));
        }
        return urls;
    }

    /**
     * format of the output of the crawler
     *
     * @param urls the urls on the web page fetched
     * @param url the url of tthe fetched page
     * @return the string of urls on the page
     */
    public static String formatOutput(String url, Set<String> urls) {
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        builder.append(System.lineSeparator());
        builder.append("\t");
        builder.append(String.join(System.lineSeparator() + "\t", urls));
        return builder.toString();
    }

    /**
     * Checks if the first command line arg is a valid url by casting
     *
     * @param stringUrl the string to test
     * @return true if is valid url, false otherwise
     */
    public static boolean isValidUrl(String stringUrl) {
        try {
            URL url = new URL(stringUrl);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
