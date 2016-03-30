# Linkbait core

Core library for building plugins for [Linkbait](https://gitlab.com/nishtahir/linkbait).

# Usage

This library is made available in through the following maven repository. Simply include it in your build script like so.
```
repositories {
    maven { url = 'https://gitlab.com/nishtahir/linkbait-core/raw/releases/'}
}

dependencies {
    compile 'com.nishtahir.linkbait:linkbait-core:0.1.1'
}
```

In a new class, simply extend `RequestHandler` and include your business logic.

For example,

```
class MyHandler implements RequestHandler<String, SlackMessagePosted> {

    @Override
    String parse(String message, String sessionId) {
        // Parse content here
        // Optionally throw an exception if parse failed
        return result of parsing
    }

    @Override
    boolean handle(SlackSession session, SlackMessagePosted event) {
        //Invoke parse here
        String result = parse(event.getMessageContent(), session.sessionPersona().getId());

        //Do something here
        session.sendMessage(event.getChannel(), "Successfully parsed message", null)
    }
}
```


To prepare a package for use with Linkbait, You need to include the following file named `plugin.json` in the route of your `resources` folder.
```
{
  "version": "1.0",
  "title": "N plugin",
  "description": "React with :nutella: whenever N is mentioned",
  "author": "Nish Tahir",
  "url": "",
  "handler": "com.nishtahir.linkbait.NReactionHandler"
}
```

Finally simply, package the application as a `*.jar` file and drop it in the `plugins` folder of a Linkbait installation and restart the server.

# License

Copyright 2016 Nish Tahir

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
