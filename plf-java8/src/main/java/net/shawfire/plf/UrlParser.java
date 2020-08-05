package net.shawfire.plf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlParser {

    private static Logger logger = LoggerFactory.getLogger(UrlParser.class);

    private static final int URL_FIELD = 5;

    /**
     * "[^\\/]* " - Match prefix chars other than "/" followed by a space (e.g. "GET ")
     * "([^ ]*)"  = Match URL chars other than space " " (e.g. "http://google.com" or "/google.com")
     * ".*" - Math suffix other characters other than the URL (e.g. " HTTP/1.1")
     */
    private static final String URL_REGEX = "[^\\/]* ([^ ]*).*";

    private Map<String, Integer> urlCount = new HashMap<>();

    private static final Pattern urlPattern = Pattern.compile(URL_REGEX);

    String[] getMostVisitedURLs(int limit) {
        String[] mostVisitedURLs = ParseUtils.getMostFrequent(urlCount, limit);
        logger.info("The top {} most visited URLs: {}",  limit, mostVisitedURLs );
        return mostVisitedURLs;
    }

    Map<String, Integer> getMostVisitedURLsCounts(int limit) {
        Map<String, Integer> mostVisitedURLCounts = ParseUtils.sortByValue(urlCount, limit);
        return mostVisitedURLCounts;
    }

    public void addIfUrl(String field, int count) {
        if (count == URL_FIELD) {
            String url = parseUrl(field);
            logger.debug("field[{}]: {}, URL: \"{}\"", count, field, url);
            ParseUtils.addItemToCount(urlCount, url);
        }
    }

    static String parseUrl(String field) {
        Matcher urlMatcher = urlPattern.matcher(field);
        if (urlMatcher.find()) {
            return urlMatcher.group(1);
        } else {
            return "";
        }
    }
}
