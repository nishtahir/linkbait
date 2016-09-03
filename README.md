# Linkbait

Linkbait is an extensible bot framework with supports for plugins.

# Supported platforms

* [Slack](https://slack.com/)

* [Discord](https://discordapp.com/)

# Getting started

To get started with Linkbait, you have the option of pulling a docker image from the
container registry, or building from source.

If your aim is to contribute to Linkbait, please skip to builing building from source.

## Docker

Linkbait is made available though the gitlab container registry. Running the following command

```
docker pull registry.gitlab.com/nishtahir/linkbait:latest
```
will get you the latest version of linkbait.

Run the image using

```
docker run -d -P --name [name] -v [path to data]:/data registry.gitlab.com/nishtahir/linkbait [args] 
```

Here you mount the `data` directory which contains the plugins, static files and databases

> Plugins are not distributed in the container at this time. but will be in a future release.

## Building from source

### Requirements

In order to build Linkbait, you need some variant of the Java Development Kit (JDK). However some packages require
the JavaFX library.

If you intend to build Linkbait with the OpenJDK, be sure also install OpenJFX. On Ubuntu this can be done using apt

```
apt-get update && apt-get install -y --no-install-recommends openjfx
```

Clone the repository run the following command in the project root directory

``` sh
./gradlew installDist
```

This will build and deploy Linkbait into the `build/linkbait` directory.

### Basic usage

``` sh
Usage: <linkbait> [options] [command] [command options]
  Options:
    --help, -h, Display this message

  Commands:
    server      Start a linkbait server instance
      Usage: server [options]
        Options:
          --port, -p
             Port number
             Default: 4567
          --plugin, Plugin directory

             Default: plugins/
          --repo, Plugin repository directory

             Default: repo/
          --static, Static file directory

             Default: static/
          --temp, Temporary file directory

             Default: temp/

    bot      Start a standalone bot instance
      Usage: bot [options]
        Options:
        * --id, i, Bot identifier

          --plugin, Plugin directory

             Default: plugins/
          --repo, Plugin repository directory

             Default: repo/
        * --key, k, Service api key or token

        * --service, s, Service to connect to

          --static, Static file directory

             Default: static/
          --temp, Temporary file directory

             Default: temp/
```

Linkbait can be run as a standalone single bot instance or as a bot server
running multiple bots as services. As a standalone bot you must provide an Id and
API key and specify the service in order to run Linkbait.

# Extending Linkbait with plugins

Linkbait plugins are distributed as `*.jar` (java archives). All plugins should be placed
in a *plugin* folder and set using the `--plugin` flag when launching the bot or server.

Plugins can be developed using any JVM language by implementing the [Plugin API](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugin-api).

Plugins that are avaliable with Linkbait

* [Pokedex](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugins/plugin-pokedex)
* [Memegen](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugins/plugin-memegen)
* [HeySnackfood](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugins/plugin-hey-snackfood)
* [RedditAutocomplete](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugins/plugin-reddit-autocomplete)
* [Storebot](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugins/plugin-reddit-autocomplete)
* [Imdb](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugins/plugin-imdb)
* [Orly](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugins/plugin-orly)
* [Tags](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugins/plugin-tags) 
* [AsciiText](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugins/plugin-asciitext)

# Architecture

![LinkbaitArchitecture](/uploads/d934b724ec8a31a4c3d2e0cad8e87394/LinkbaitArchitecture.png)

Linkbait is made of a lot of smaller stuff that come together to make up the framework

* [Linkbait-core](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-core) - This is the heart of the bot (where the magic happens). This is where
connections to the various messaging providers are made.
* [Linkbait-server](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-server) - This is an application server for Linkbait.
* [Linkbait-plugin-api](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugin-api) - Used to develop plugins which are loaded by the core.
* [Linkbait-www](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-www) - The web interface served by the server.

# Contributors

Thanks to these wonderful people, that have helped make Linkbait possible

* Andreas Backx aka [@AndreasBackx](https://twitter.com/AndreasBackx)
* Max Keller aka [@langer_hans](https://twitter.com/langer_hans)
* Aki Kanellis aka [@AkiKanellis](https://twitter.com/AkiKanellis)
* Ben Butzow aka [@cr5315](https://twitter.com/cr5315)
* Jason Wyatt Feinstein aka [@jasonwyatt](twitter.com/jasonwyatt)

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
