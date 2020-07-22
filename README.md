# parse-log-file

<details><summary>Regular expression parsing</summary>

[Online regular expression parsing](https://regex101.com/)

- expample line in log file

```
177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] "GET /intranet-analytics/ HTTP/1.1" 200 3574 "-" "Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7"
```

- match with ip address and anything else

```regex
^(\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\.|$)){4}\b)?.*$
```

- Just separate into fields
  - fields separated by spaces
  - fields contained in [] or ""

```regex
((\[[^\]]*\])|("[^"]*")|([^ ]*))*
```

```regex
(\[[^\]]*\]|"[^"]*"|[^ ]*)*
```

</details>
