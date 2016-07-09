# linkbait-plugin-api

[![build status](https://gitlab.com/nishtahir/linkbait-plugin-api/badges/master/build.svg)](https://gitlab.com/nishtahir/linkbait-plugin-api/commits/master)

This api is used to develop plugins for the Linkbait platform.

# Usage

This library is available in through JitPack. Simply include it in your build script like so.

```
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.gitlab.nishtahir:linkbait-plugin-api:-SNAPSHOT'
}
```

# Plugin manifest

In order for the framework to properly load your plugin, its properties must be defined in the manifest.
This can be done through Gradle using the jar 

```
jar {
  baseName = 'MySuperPoweredPlugin'
  version = '9001'
  manifest {
    attributes 'Plugin-Class' : 'com.mycompany.MyPlugin',
      'Plugin-Id' : 'MySuperPoweredPlugin',
      'Plugin-Version' : '1.0',
      'Plugin-Provider' : 'Your name here <Optional email>'
  }
}
```
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