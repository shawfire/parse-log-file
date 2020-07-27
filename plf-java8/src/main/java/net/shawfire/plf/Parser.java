package net.shawfire.plf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {

    private static Logger logger = LoggerFactory.getLogger(Parser.class);

    /**
     * Allow for three types of fields
     *  1. A field enclosed in square brackets
     *     "\[[^\]]*\]" - Match "[" - then a chars other than "]" terminate field with "]"
     *     note: the pipe character "|" signifies an "or" another type of field
     *  2. A field enclosed in double quotes
     *     "\"[^"]*" -  Match `"` - then a chars other than `"` terminate field with `"`
     *  3. A field that does not contain a space
     *     [^ ]*)* - Match a field that does not contain spaces
     */
    private static final String FIELD_REGEX = "(\\[[^\\]]*\\]|\"[^\"]*\"|[^ ]*)*";

    /**
     * "[^\\/]* " - Match prefix chars other than "/" followed by a space (e.g. "GET ")
     * "([^ ]*)"  = Match URL chars other than space " " (e.g. "http://google.com" or "/google.com")
     * ".*" - Math suffix other characters other than the URL (e.g. " HTTP/1.1")
     */
    private static final String URL_REGEX = "[^\\/]* ([^ ]*).*";

    private static final Pattern urlPattern = Pattern.compile(URL_REGEX);

    private Map<String, Integer> urlCount = new HashMap<>();
    private Map<String, Integer> ipAddressCount = new HashMap<>();

    void addItemToCount(Map<String, Integer> countMap, String field) {
        int count = 1;
        if (countMap.containsKey(field)) {
            count += countMap.get(field);
            countMap.replace(field, count);
        }
        else {
            countMap.put(field, count);
        }
    }

    public static Map<String, Integer> sortByValue(final Map<String, Integer> keyCounts, int limit) {
        return keyCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static String[] getMostFrequent(final Map<String, Integer> keyCounts, int limit) {
        List<String> keys = keyCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
        String[] keyArray = keys.toArray(new String[keys.size()]);
        return keyArray;
    }

    String[] parse(String line) {
        logger.debug("Parse {}", line);
        ArrayList<String> fieldsList = new ArrayList<>();
        Pattern p = Pattern.compile(FIELD_REGEX);
        //  get a matcher object
        Matcher m = p.matcher(line);
        int count = 0;
        while(m.find()) {
            String field = line.substring(m.start(), m.end());
            if (field.trim().length() == 0) {
                continue;
            }
            count++;
            fieldsList.add(field);
            if (count == 5) {
                String url = parseUrl(field);
                logger.debug("field[{}]: {}, URL: \"{}\"", count, field, url);
                addItemToCount(urlCount, url);
            }
            if (count == 1) {
                addItemToCount(ipAddressCount, field);
            }
        }
        String[] parsedFields = fieldsList.toArray(new String[fieldsList.size()]);
        logger.debug("Parsed to: {}", Arrays.toString(parsedFields));
        return parsedFields;
    }

    String parseUrl(String field) {
        Matcher urlMatcher = urlPattern.matcher(field);
        if (urlMatcher.find()) {
            return urlMatcher.group(1);
        } else {
            return "";
        }
    }

    int getNumberOfUniqueUrls() {
        int uniqueCount = 0;
        for (Map.Entry<String, Integer> entry : ipAddressCount.entrySet()) {
            if (entry.getValue() == 1) {
                uniqueCount++;
            }
        }
        logger.info("Number of unique IP addresses: {}",  uniqueCount );
        return uniqueCount;
    }

    String[] getMostActiveIPs(int limit) {
        String[] mostActiveIPs = getMostFrequent(ipAddressCount, limit);
        logger.info("The top {} most active IP addresses: {}",  limit, mostActiveIPs );
        return mostActiveIPs;
    }

    Map<String, Integer> getMostActiveIPCounts(int limit) {
        Map<String, Integer> mostActiveIPCounts = sortByValue(ipAddressCount, limit);
        return mostActiveIPCounts;
    }

    String[] getMostVisitedURLs(int limit) {
        String[] mostVisitedURLs = getMostFrequent(urlCount, limit);
        logger.info("The top {} most visited URLs: {}",  limit, mostVisitedURLs );
        return mostVisitedURLs;
    }

    Map<String, Integer> getMostVisitedURLsCounts(int limit) {
        Map<String, Integer> mostVisitedURLCounts = sortByValue(urlCount, limit);
        return mostVisitedURLCounts;
    }

}
