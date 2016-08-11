# Linkbait

Linkbait is an extensible bot framework supports for plugins.

## Getting started

## Building from source

Clone the repository and run the following command in the project root directory

``` sh

./gradlew installDist

```

This will build Linkbait and deploy it in the build folder.

## Basic usage

``` sh
Usage: <main class> [options] [command] [command options]
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

# Architecture

![LinkbaitArchitecture](/uploads/d934b724ec8a31a4c3d2e0cad8e87394/LinkbaitArchitecture.png)

Linkbait is made of a lot of smaller stuff that come together to make up the framework

* Linkbait-core - This is the heart of the bot (where the magic happens). This is where
connections to the various messaging providers are made.
* Linkbait-server - This is an application server for Linkbait.
* Linkbait-plugin-api - Used to develop plugins which are loaded by the core.
* Linkbait-www - The web interface served by the server.

# Contributors

Thanks to these wonderful people, that have helped make Linkbait possible

* Andreas Backx aka [@AndreasBackx](https://twitter.com/AndreasBackx)
* Max Keller aka [@langer_hans](https://twitter.com/langer_hans)
* Aki Kanellis aka [@AkiKanellis](https://twitter.com/AkiKanellis)
* Ben Butzow aka [@cr5315](https://twitter.com/cr5315)

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
