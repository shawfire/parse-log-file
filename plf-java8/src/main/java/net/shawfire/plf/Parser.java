package net.shawfire.plf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static String FileNameFoundMsg = "File [%1$s] is not found.";

    private static Logger logger = LoggerFactory.getLogger(Parser.class);

    /**
     * Allow for three types of fields
     *  1. A field enclosed in square brackets
     *     "\[[^\]]*\]" - Match "[" - then a chars other than "]" terminate field with "]"
     *     note: the pipe character "|" signifies an "or" another type of field
     *  2. A field enclosed in double quotes
     *     "\"[^"]*" -  Match `"` - then a chars other than `"` terminate field with `"`
     *  3. A field that does not contain a space
     *     [^ ]* - Match a field that does not contain spaces
     */
    private static final String FIELDS_REGEX = "(\\[[^\\]]*\\]|\"[^\"]*\"|[^ ]*)*";

    private UrlParser urlParser = new UrlParser();
    private IPAddressParser ipAddressParser = new IPAddressParser();

    public UrlParser getUrlParser() {
        return urlParser;
    }

    public IPAddressParser getIpAddressParser() {
        return ipAddressParser;
    }

    public String[] parseLine(String line) {
        logger.debug("Parse {}", line);
        ArrayList<String> fieldsList = new ArrayList<>();
        Pattern p = Pattern.compile(FIELDS_REGEX);
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
            urlParser.addIfUrl(field, count);
            ipAddressParser.addIfUrl(field, count);
        }
        String[] parsedFields = fieldsList.toArray(new String[fieldsList.size()]);
        logger.debug("Parsed to: {}", Arrays.toString(parsedFields));
        return parsedFields;
    }

    private void readFromInputStream(InputStream inputStream)
            throws IOException {
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null && (line = line.trim()).length() != 0) {
                parseLine(line);
            }
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readFromInputStream(String fileName)
            throws IOException {
        InputStream inputStream = Parser.class.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException(String.format(FileNameFoundMsg, fileName));
        }
        readFromInputStream(inputStream);
    }

}
