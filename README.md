# parse-log-file

Parse Log File Project

- [Requirements: programming-task.pdf](./programming-task.pdf)
- [Java 8 Maven Project Solution ./plf-java8/README.md](./plf-java8/README.md)

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
