# linkbait-plugin-api

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