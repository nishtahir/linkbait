# Tenor (gif) plugin

Allows to search the Tenor API for relevant gifs for a search term.

# Usage

`@linkbait gif <search term>`

# Implementation details
The API is handled via Retrofit. Jackson is used for the deserialization. A custom deserializer has been added to handle the APIs
lack of proper HTTP status codes for errors. While all API calls have been implemented only the search feature is currently in use.