# Linkbait server

This contains the server for the *Linkbait* project. It aims to offer Linkbait as a microservice. And provide a webapp and RESTFul api to interact with it.

The server uses an embedded Derby database for data persistence


## Usage

```sh
usage: linkbait [options]
 -h,--help                     Show help message.
 -p,--port <number>            Select a port to run on
    --plugin-dir <directory>   Plugin file directory
    --repo-dir <directory>     Repo directory
    --static-dir <directory>   Static file directory
    --temp-dir <directory>     Temp file directory
```

# Related projects

* [linkbait-core](https://gitlab.com/nishtahir/linkbait-core)

* [linkbait](https://gitlab.com/nishtahir/linkbait)

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