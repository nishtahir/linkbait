# Linkbait

## Getting started

## Basic usage

```sh
usage: groovy
 -c,--config <arg>   configuration file
 -p,--port <arg>     port to listen on

```

## Using the gradle runner

To run the application using gradle

```sh
 ./gradlew run -Dexec.args="-p=4567 -c=Path/to/config.yml"
```

note the `=` between the flag and the argument

## As a java application

```sh
java -jar -p 4567 -c Path/to/config.yml
```

# Configuring the client
A config file is required to run the client, and this was totally not a sneaky way to avoid committing my slack API token.
The config file is expected to be a `*.yml` file. The following is a sample

```yaml
# For convenience
version:    1.0

# You can only have 1 db
connection:
    url:    jdbc:sqlite:my-database.sqlite

# You can have more than one team
teams:
    MyAwesomeSlackTeam:        mYrEALaPItOKEN-wHICHmAYbEpROPERLYcAMELcASED

```

# Deploying the application

First you need to clean, build, package and install the App like a boss

```sh
./gradlew clean install
```

This will create an install directory in the build folder. Now all you need to do
is run the start script passing in the args. If you are building the multi-project variant,
the server is located in the `linkbait-server` folder in the build directory

```sh
./build/linkbait-server/install/linkbait-server/bin/linkbait-server -c [config-file] [other args]
```

# Creating a plugin for Linkbait

Linkbait's plugin system allows you to write and load plugins dynamically without needing
to build them into the project. Plugins are loaded from jars placed in the configuration
path specified in the `plugins` tag.

To create a plugin for Linkbait the [linkbait core](https://gitlab.com/nishtahir/linkbait-core/)
project must be added to your build path. This can be done using JitPack.

```
repositories {
	maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.gitlab.nishtahir:linkbait-core:-SNAPSHOT'
}
```

In order to parse and process commands you must extend `RequestHandler`. The entry point for the
plugin is the `handle` method, it is invoked when it's ready to invoke the plugin.

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

All plugins need to specify a `plugin.json` file in the root of the `resources`
folder and must be packaged into the distributed `jar`. For example,

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

While the other properties are mostly metadata, the `handler` property must be the fully
qualified class name of class that implements `RequestHandler`.

#Debugging plugins

To debug a plugin using the IDEA debugger, a few adjustments need to be made to your project and run configuration. This assumes that your plugin project is open in IDEA with Linkbait added as a module.

* Your `config.yml` file's plugin folder should point to install directory of the server build folder.

  ```
  plugins: /build/linkbait-server/install/linkbait-server/plugins
  ```
  >  You could use the absolute path, just to be safe

* You need to specify a `Before launch` task in IDEA. This should be a `Gradle task`
 with the following properties.

 ```
 Gradle project: linkbait
 Tasks: install
 Script parameters: -x test (optional: unless you want to run tests everytime you run the app)
 ```

 With this configuration, debug markers should work in your plugin projects.

# Contributors

Thanks to these wonderful people, that have helped make Linkbait possible

* Andreas Backx aka [@AndreasBackx](https://twitter.com/AndreasBackx)
* Max Keller aka [@langer_hans](https://twitter.com/langer_hans)
* Aki Kanellis aka [@AkiKanellis](https://twitter.com/AkiKanellis)
* Ben Butzow aka [@cr5315](https://twitter.com/cr5315)

# Related projects

* [linkbait server](https://gitlab.com/nishtahir/linkbait-server)

* [linkbait core](https://gitlab.com/nishtahir/linkbait-core)


LICENSE
=======

Copyright 2015 Nish Tahir

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
