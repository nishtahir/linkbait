# Linkbait Pokedex plugin 

Get a pokedex in your chat.

## Usage
``` sh
[botname] pokedex [name|id|emoji]
```

## Screenshot

![pokedex_sample](/uploads/f677b9b7f81fbe83c3554cdf92451d50/pokedex_sample.png)

## Linkbait plugin development
This plugin serves demonstrates how to write plugins for linkbait. It is assumed
that you are building using gradle as other build tools are not supported
at this time.

### Setting up the Manifest

The manifest contains the plugin metadata that will be used to load the plugin at
runtime. This allows plugins to be started and stopped without affecting operation.

The following attributes must be included in your manifest.

* `Plugin-Class` - Entrypoint for the plugin, which extends `Plugin`

* `Plugin-Id` - Unique Id for your plugin. It could be your packagename

* `Plugin-Version` - Version number

* `Plugin-Provider` - Author. could also include email in the form `Author <Email>`

* Dependencies - Comma separated list of `compile` time dependencies. **Jars which are
  available on maven central do not have to be bundled but must be declared. The 
  framework will take care of downloading and adding them along with transitive dependencies
  to the classpath.

> Bundled jars should be excluded from the dependency list. 


``` java
jar.doFirst() {
    baseName = "PluginPokedex"
    version = "1.0"
    manifest {
        attributes "Plugin-Class": "com.nishtahir.linkbait.pokedex.PokedexPlugin",
                "Plugin-Id": "com.nishtahir.linkbait.pokedex",
                //Strictly enforces semver(major.minor.patch)
                "Plugin-Version": "1.0.0",
                "Plugin-Provider": "Nish Tahir",
                //Export a list of compile time dependencies.
                //This is so that they can be added to the
                //classpath at runtime
                "Dependencies": configurations.compile.allDependencies.collect {
                    "${it.group}:${it.name}:${it.version}"
                }.join(',')

    }
}
```