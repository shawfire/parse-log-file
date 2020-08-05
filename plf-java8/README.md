# parse-log-file: plf-java8

## IntelliJ Maven Java 8 Solution

- Implemented a Parser class which satisfied the solution requirements
- JUnit 4 is used to test the parsing of each line and field as well as the solution requirements
- SL4J Logger is used to log info output for the solution
  - There is more debug logging, but this is currently switched off.
- An App class is included which
  - instantiates `Parser` (`parser`)
  - parses each log file line via `parser.parseLine(logFileLineString)`
  - then each of the routines are called to produce the solution output via info log messages

<details><summary>Main App</summary>

[App class which takes the logFile to parse as a parameter](./src/main/java/net/shawfire/plf/App.java)

</details>

<details><summary>Main App / Main StubApp / Test Logger Output</summary>

- The Main App, Main StubApp and the Unit tests produce essentially the same logging output.

```
0    [main] INFO  net.shawfire.plf.Parser  - Number of unique IP addresses: 4
79   [main] INFO  net.shawfire.plf.Parser  - The top 3 most visited URLs: [/docs/manage-websites/, /blog/2018/08/survey-your-opinion-matters/, /newsletter/]
79   [main] INFO  net.shawfire.plf.Parser  - The top 3 most active IP addresses: [168.41.191.40, 50.112.00.11, 177.71.128.21]
```

</details>

<details><summary>Main StubApp</summary>

[StubApp class which does not rely on a logFile as input](./src/main/java/net/shawfire/plf/StubApp.java)

</details>

<details><summary>Parser Class</summary>

[Parser class which reads the logFile and parses it accordingly](./src/main/java/net/shawfire/plf/Parser.java)

</details>

<details><summary>IP Address Parser Class </summary>

[IP Address Parser class which contains all the logic specific to parsing and reporting IP Addresses](./src/main/java/net/shawfire/plf/IPAddressParser.java)

</details>

<details><summary>URL Parser Class </summary>

[URL Parser class which contains all the logic specific to parsing and reporting URLs](./src/main/java/net/shawfire/plf/UrlParser.java)

</details>

<details><summary>Parser Utils Class </summary>

[Parser Utils class which contains generic static pure parsing functions](./src/main/java/net/shawfire/plf/ParserUtils.java)

</details>

<details><summary>Parse Class Tests</summary>

[ParseTest class which contains Parse class JUnit4 tests](./src/test/java/net/shawfire/plf/ParserTest.java)

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
```

- Extract from [`src/main/java/net/shawfire/plf/IPAddressParser.java`](./src/main/java/net/shawfire/plf/IPAddressParser.java)

```java
    /**
     * "[^\\/]* " - Match prefix chars other than "/" followed by a space (e.g. "GET ")
     * "([^ ]*)"  = Match URL chars other than space " " (e.g. "http://google.com" or "/google.com")
     * ".*" - Math suffix other characters other than the URL (e.g. " HTTP/1.1")
     */
    private static final String URL_REGEX = "[^\\/]* ([^ ]*).*";
```

</details>
