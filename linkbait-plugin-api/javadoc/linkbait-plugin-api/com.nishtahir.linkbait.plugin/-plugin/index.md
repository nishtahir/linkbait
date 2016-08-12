[data](../../index.md) / [com.nishtahir.linkbait.plugin](../index.md) / [Plugin](.)


# Plugin

`abstract class Plugin` [(source)](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugin-api/src/main/kotlin//com/nishtahir/linkbait/plugin/Plugin.kt#L6)

Base implementation of a plugin




### Constructors


| [&lt;init&gt;](-init-.md) | `Plugin()`
Base implementation of a plugin

 |


### Functions


| [onPluginStateChanged](on-plugin-state-changed.md) | `abstract fun onPluginStateChanged(): Unit` |
| [start](start.md) | `abstract fun start(context:&nbsp;[PluginContext](../-plugin-context/index.md)): Unit` |
| [stop](stop.md) | `abstract fun stop(context:&nbsp;[PluginContext](../-plugin-context/index.md)): Unit` |

