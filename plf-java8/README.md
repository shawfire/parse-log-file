# parse-log-file: plf-java8

## IntelliJ Maven Java 8 Solution

- Implemented a Parser class which satisfied the solution requirements
- JUnit 4 is used to test the parsing of each line and field as well as the solution requirements
- SL4J Logger is used to log info output for the solution
  - There is more debug logging, but this is currently switched off.
- An App class is included which
  - instantiates `Parser` (`parser`)
  - parses each log file line via `parser.parse(logFileLineString)`
  - then each of the routines are called to produce the solution output via info log messages

<details><summary>Main App</summary>

```java
package net.shawfire.plf;

public class App {

    static String[] logRecords = {
            "177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] \"GET /intranet-analytics/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7\"\n",
            "168.41.191.40 - - [09/Jul/2018:10:11:30 +0200] \"GET http://example.net/faq/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Linux; U; Android 2.3.5; en-us; HTC Vision Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1\"\n",
            "168.41.191.41 - - [11/Jul/2018:17:41:30 +0200] \"GET /this/page/does/not/exist/ HTTP/1.1\" 404 3574 \"-\" \"Mozilla/5.0 (Linux; U; Android 2.3.5; en-us; HTC Vision Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1\"\n",
            "168.41.191.40 - - [09/Jul/2018:10:10:38 +0200] \"GET http://example.net/blog/category/meta/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.24 (KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24\"\n",
            "177.71.128.21 - - [10/Jul/2018:22:22:08 +0200] \"GET /blog/2018/08/survey-your-opinion-matters/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6\"\n",
            "168.41.191.9 - - [09/Jul/2018:23:00:42 +0200] \"GET /docs/manage-users/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3\"\n",
            "168.41.191.40 - - [09/Jul/2018:10:11:56 +0200] \"GET /blog/category/community/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; ca-ad) AppleWebKit/531.2+ (KHTML, like Gecko) Safari/531.2+ Epiphany/2.30.6\"\n",
            "168.41.191.34 - - [10/Jul/2018:22:01:17 +0200] \"GET /faq/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7\"\n",
            "177.71.128.21 - - [10/Jul/2018:22:21:03 +0200] \"GET /docs/manage-websites/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0\"\n",
            "50.112.00.28 - - [11/Jul/2018:15:49:46 +0200] \"GET /faq/how-to-install/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; ca-ad) AppleWebKit/531.2+ (KHTML, like Gecko) Safari/531.2+ Epiphany/2.30.6\"\n",
            "50.112.00.11 - admin [11/Jul/2018:17:31:56 +0200] \"GET /asset.js HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6\"\n",
            "72.44.32.11 - - [11/Jul/2018:17:42:07 +0200] \"GET /to-an-error HTTP/1.1\" 500 3574 \"-\" \"Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0\"\n",
            "72.44.32.10 - - [09/Jul/2018:15:48:07 +0200] \"GET / HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0\" junk extra\n",
            "168.41.191.9 - - [09/Jul/2018:22:56:45 +0200] \"GET /docs/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; Linux i686; rv:6.0) Gecko/20100101 Firefox/6.0\" 456 789\n",
            "168.41.191.43 - - [11/Jul/2018:17:43:40 +0200] \"GET /moved-permanently HTTP/1.1\" 301 3574 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.24 (KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24\"\n",
            "168.41.191.43 - - [11/Jul/2018:17:44:40 +0200] \"GET /temp-redirect HTTP/1.1\" 307 3574 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.24 (KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24\"\n",
            "168.41.191.40 - - [09/Jul/2018:10:12:03 +0200] \"GET /docs/manage-websites/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; Linux i686; rv:6.0) Gecko/20100101 Firefox/6.0\"\n",
            "168.41.191.34 - - [10/Jul/2018:21:59:50 +0200] \"GET /faq/how-to/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/5.0)\"\n",
            "72.44.32.10 - - [09/Jul/2018:15:49:48 +0200] \"GET /translations/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5\"\n",
            "79.125.00.21 - - [10/Jul/2018:20:03:40 +0200] \"GET /newsletter/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/5.0)\"\n",
            "50.112.00.11 - admin [11/Jul/2018:17:31:05 +0200] \"GET /hosting/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6\"\n",
            "72.44.32.10 - - [09/Jul/2018:15:48:20 +0200] \"GET /download/counter/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86; en-US) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7\"\n",
            "50.112.00.11 - admin [11/Jul/2018:17:33:01 +0200] \"GET /asset.css HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6\"\n"
    };

    public static void main(String[] args) {

        Parser parser = new Parser();
        for (int i=0; i < logRecords.length; i++) {
            parser.parse(logRecords[i]);
        }

        // FIRST REQUIREMENT: The number of unique IP addresses
        parser.getNumberOfUniqueUrls();

        // SECOND REQUIREMENT: The top 3 most visited URLs
        int limit = 3;
        parser.getMostVisitedURLs(limit);

        // THIRD REQUIREMENT: The top 3 most active addresses
        parser.getMostActiveIPs(limit);

    }

}

```

</details>

<details><summary>Main App / Test Logger Output</summary>

- The Main App and the Unit tests produce the same logging output.

```
0    [main] INFO  net.shawfire.plf.Parser  - Number of unique IP addresses: 4
79   [main] INFO  net.shawfire.plf.Parser  - The top 3 most visited URLs: [/docs/manage-websites/, /blog/2018/08/survey-your-opinion-matters/, /newsletter/]
79   [main] INFO  net.shawfire.plf.Parser  - The top 3 most active IP addresses: [168.41.191.40, 50.112.00.11, 177.71.128.21]
```

</details>

<details><summary>Parse Class</summary>

```java
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

```

</details>

<details><summary>Parse Class Tests</summary>

```java
package net.shawfire.plf;

import org.junit.Assert;
import org.junit.Test;


import java.util.Map;

import static org.junit.Assert.*;

public class ParserTest {


    @Test
    public void parseOneRecord() {
        Parser parser = new Parser();
        String[] actualFields = parser.parse(
                "177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] " +
                "\"GET /intranet-analytics/ HTTP/1.1\" 200 3574 \"-\" " +
                "\"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7\"");
        String[] expectedFields = {
                "177.71.128.21", "-", "-", "[10/Jul/2018:22:21:28 +0200]",
                "\"GET /intranet-analytics/ HTTP/1.1\"", "200", "3574", "\"-\"",
                "\"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7\""
        };
        Assert.assertEquals("Unexpected number of fields parsed. ", expectedFields.length, actualFields.length);
        for (int i = 0; i < expectedFields.length; i++) {
            Assert.assertEquals(String.format("line %1$s: ", i), expectedFields[i], actualFields[i]);
        }
        Assert.assertEquals("/intranet-analytics/" ,parser.parseUrl(actualFields[4]));
    }

    @Test
    public void parseMultipleRecords() {
        String[] logRecords = {
                "177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] \"GET /intranet-analytics/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7\"\n",
                "168.41.191.40 - - [09/Jul/2018:10:11:30 +0200] \"GET http://example.net/faq/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Linux; U; Android 2.3.5; en-us; HTC Vision Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1\"\n",
                "168.41.191.41 - - [11/Jul/2018:17:41:30 +0200] \"GET /this/page/does/not/exist/ HTTP/1.1\" 404 3574 \"-\" \"Mozilla/5.0 (Linux; U; Android 2.3.5; en-us; HTC Vision Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1\"\n",
                "168.41.191.40 - - [09/Jul/2018:10:10:38 +0200] \"GET http://example.net/blog/category/meta/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.24 (KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24\"\n",
                "177.71.128.21 - - [10/Jul/2018:22:22:08 +0200] \"GET /blog/2018/08/survey-your-opinion-matters/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6\"\n",
                "168.41.191.9 - - [09/Jul/2018:23:00:42 +0200] \"GET /docs/manage-users/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3\"\n",
                "168.41.191.40 - - [09/Jul/2018:10:11:56 +0200] \"GET /blog/category/community/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; ca-ad) AppleWebKit/531.2+ (KHTML, like Gecko) Safari/531.2+ Epiphany/2.30.6\"\n",
                "168.41.191.34 - - [10/Jul/2018:22:01:17 +0200] \"GET /faq/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7\"\n",
                "177.71.128.21 - - [10/Jul/2018:22:21:03 +0200] \"GET /docs/manage-websites/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0\"\n",
                "50.112.00.28 - - [11/Jul/2018:15:49:46 +0200] \"GET /faq/how-to-install/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; ca-ad) AppleWebKit/531.2+ (KHTML, like Gecko) Safari/531.2+ Epiphany/2.30.6\"\n",
                "50.112.00.11 - admin [11/Jul/2018:17:31:56 +0200] \"GET /asset.js HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6\"\n",
                "72.44.32.11 - - [11/Jul/2018:17:42:07 +0200] \"GET /to-an-error HTTP/1.1\" 500 3574 \"-\" \"Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0\"\n",
                "72.44.32.10 - - [09/Jul/2018:15:48:07 +0200] \"GET / HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0\" junk extra\n",
                "168.41.191.9 - - [09/Jul/2018:22:56:45 +0200] \"GET /docs/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; Linux i686; rv:6.0) Gecko/20100101 Firefox/6.0\" 456 789\n",
                "168.41.191.43 - - [11/Jul/2018:17:43:40 +0200] \"GET /moved-permanently HTTP/1.1\" 301 3574 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.24 (KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24\"\n",
                "168.41.191.43 - - [11/Jul/2018:17:44:40 +0200] \"GET /temp-redirect HTTP/1.1\" 307 3574 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.24 (KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24\"\n",
                "168.41.191.40 - - [09/Jul/2018:10:12:03 +0200] \"GET /docs/manage-websites/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; Linux i686; rv:6.0) Gecko/20100101 Firefox/6.0\"\n",
                "168.41.191.34 - - [10/Jul/2018:21:59:50 +0200] \"GET /faq/how-to/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/5.0)\"\n",
                "72.44.32.10 - - [09/Jul/2018:15:49:48 +0200] \"GET /translations/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5\"\n",
                "79.125.00.21 - - [10/Jul/2018:20:03:40 +0200] \"GET /newsletter/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/5.0)\"\n",
                "50.112.00.11 - admin [11/Jul/2018:17:31:05 +0200] \"GET /hosting/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6\"\n",
                "72.44.32.10 - - [09/Jul/2018:15:48:20 +0200] \"GET /download/counter/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86; en-US) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7\"\n",
                "50.112.00.11 - admin [11/Jul/2018:17:33:01 +0200] \"GET /asset.css HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6\"\n"
        };
        Parser parser = new Parser();
        for (int i=0; i < logRecords.length; i++) {
            String[] actualFields = parser.parse(logRecords[i]);
            Assert.assertTrue("Unexpected number of fields parsed. ", actualFields.length >= 9);
        }
        // TEST FIRST REQUIREMENT: The number of unique IP addresses
        int expectedNumberOfUniqueURLs = 4;
        Assert.assertEquals("Unexpected number of unique URLs",
                expectedNumberOfUniqueURLs, parser.getNumberOfUniqueUrls());

        // TEST SECOND REQUIREMENT: The top 3 most visited URLs
        int limit = 3;
        String[] actualMostVisitedURLs = parser.getMostVisitedURLs(limit);
        Assert.assertEquals("Unexpected number of URLs returned",
                limit, actualMostVisitedURLs.length);
        Assert.assertEquals("Unexpected most visited URLs returned",
                "/docs/manage-websites/", actualMostVisitedURLs[0]);
        Assert.assertEquals("Unexpected most visited URLs returned",
                "/blog/2018/08/survey-your-opinion-matters/", actualMostVisitedURLs[1]);

        Map<String, Integer> actualMostVisitedURLsCounts = parser.getMostVisitedURLsCounts(limit);
        Assert.assertEquals("Unexpected number of unique URLs counts returned",
                limit, actualMostVisitedURLsCounts.size());
        Assert.assertEquals("Unexpected most visited URL count returned",
                new Integer(2), actualMostVisitedURLsCounts.get("/docs/manage-websites/"));
        Assert.assertEquals("Unexpected most visited URL count returned",
                new Integer(1), actualMostVisitedURLsCounts.get("/blog/2018/08/survey-your-opinion-matters/"));


        // TEST THIRD REQUIREMENT: The top 3 most active addresses
        String[] actualMostActiveIPs = parser.getMostActiveIPs(limit);
        Assert.assertEquals("Unexpected number of unique IPs returned",
                limit, actualMostActiveIPs.length);
        Assert.assertEquals("Unexpected most active IP returned",
                "168.41.191.40", actualMostActiveIPs[0]);
        Assert.assertEquals("Unexpected 2nd most active IP returned",
                "50.112.00.11", actualMostActiveIPs[1]);
        Assert.assertEquals("Unexpected 3rd most active IP returned",
                "177.71.128.21", actualMostActiveIPs[2]);

        Map<String, Integer> actualMostActiveIPCount = parser.getMostActiveIPCounts(limit);
        Assert.assertEquals("Unexpected number of unique URLs counts returned",
                limit, actualMostVisitedURLsCounts.size());
        Assert.assertEquals("Unexpected most active IP count returned",
                new Integer(4), actualMostActiveIPCount.get("168.41.191.40"));
        Assert.assertEquals("Unexpected most active IP count returned",
                new Integer(3), actualMostActiveIPCount.get("50.112.00.11"));
        Assert.assertEquals("Unexpected most active IP count returned",
                new Integer(3), actualMostActiveIPCount.get("177.71.128.21"));
    }

}
```

</details>

<details><summary>Regular expression parsing</summary>

[Online regular expression parsing](https://regex101.com/)

- Extract from [`src/main/java/net/shawfire/plf/Parser.java`](./src/main/java/net/shawfire/plf/Parser.java)

```java
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
```

</details>
