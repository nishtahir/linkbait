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
./gradlew clean installDist
```

This will create an install dir in the build folder. Now all you need to do
is run the start script passing in the args

```sh
./build/install/bin/islack-bot [args]
```

# LICENSE


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
