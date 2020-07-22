package net.shawfire.plf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    String[] parse(String line) {
        logger.info("Parse {}", line);
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
                logger.debug("field[{}]: {}", count, field);
            }
        }
        String[] parsedFields = fieldsList.toArray(new String[fieldsList.size()]);
        logger.info("Parsed to: {}", Arrays.toString(parsedFields));
        return parsedFields;
    }

    String parseUrl(String field) {
        Matcher urlMatcher = urlPattern.matcher(field);
        if (urlMatcher.find()) {
            return urlMatcher.group(1)
        } else {
            return "";
        }
    }
}
