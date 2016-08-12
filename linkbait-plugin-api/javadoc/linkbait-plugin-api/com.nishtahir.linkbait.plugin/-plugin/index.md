[linkbait-plugin-api](../../index.md) / [com.nishtahir.linkbait.plugin](../index.md) / [Plugin](.)

# Plugin

`abstract class Plugin` [(source)](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugin-api/src/main/kotlin//com/nishtahir/linkbait/plugin/Plugin.kt#L6)

Base implementation of a plugin

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Plugin()`<br>Base implementation of a plugin |

### Functions

| Name | Summary |
|---|---|
| [onPluginStateChanged](on-plugin-state-changed.md) | `abstract fun onPluginStateChanged(): Unit` |
| [start](start.md) | `abstract fun start(context: `[`PluginContext`](../-plugin-context/index.md)`): Unit` |
| [stop](stop.md) | `abstract fun stop(context: `[`PluginContext`](../-plugin-context/index.md)`): Unit` |
