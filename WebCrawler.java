import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {

    private final Set<String> visited_urls;
    private final ExecutorService executor;
    /** string pattern, the pattern to find in the string */
    public static final String TEXT_PATTERN = "(?s)<a[^>]*?href[^>]*?=[^>]*?\"(https?.+?)\".*?>";
    /** the compiled patter for regular expression finding */
    public static final Pattern TEXT_REGEX = Pattern.compile(TEXT_PATTERN);

    public WebCrawler() {
        this.visited_urls = new HashSet<>();
        this.executor = Executors.newFixedThreadPool(5);
    }

    public static String fetchContent(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        Scanner scanner = new Scanner(url.openStream());
        StringBuilder builder = new StringBuilder();
        while(scanner.hasNext()) {
            builder.append(scanner.next());
        }
        return builder.toString();
    }

    public static Set<String> getLinks(String url) throws IOException {
        String content = WebCrawler.fetchContent(url);
        Set<String> localLinks = new HashSet<>();
        Matcher matcher = TEXT_REGEX.matcher(content);
        while (matcher.find()) {
            localLinks.add(matcher.group(1));
        }
        return localLinks;
    }
}
