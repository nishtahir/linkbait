package com.nishtahir.linkbait.plugin

var TRIGGER = "jobs"

val PATTERN = """^$TRIGGER\s?+((\w|\s)+)(\s?+,\s?+)((\w|\s)+)$""".toRegex()

var HELP_MESSAGE = """
```
Usage:
$TRIGGER [tags], [location]
```
Searches LinkedIn and returns new job postings

"""

val LINKEDIN_SEARCH_ROUTE = "https://www.linkedin.com/jobs/search"
val USER_AGENT_KEY = "User-Agent"
val USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.22 Safari/537.36"