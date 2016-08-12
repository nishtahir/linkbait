[data](../../index.md) / [com.nishtahir.linkbait.plugin](../index.md) / [PluginContext](.)


# PluginContext

`interface PluginContext` [(source)](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugin-api/src/main/kotlin//com/nishtahir/linkbait/plugin/PluginContext.kt#L9)

Context in which plugin is currently operating. Supplied by active bot.




### Functions


| [getConfiguration](get-configuration.md) | `abstract fun getConfiguration(): [Configuration](../../com.nishtahir.linkbait.plugin.model/-configuration/index.md)` |
| [getMessenger](get-messenger.md) | `abstract fun getMessenger(): [Messenger](../-messenger/index.md)` |
| [getPluginState](get-plugin-state.md) | `abstract fun getPluginState(): Unit`
State in which plugin is operating. In case of Async operations,
Check the running state of the plugin before performing actions.

 |
| [registerListener](register-listener.md) | `abstract fun registerListener(listener:&nbsp;[EventListener](../../com.nishtahir.linkbait.plugin.model/-event-listener.md)): Unit` |
| [unregisterListener](unregister-listener.md) | `abstract fun unregisterListener(listener:&nbsp;[EventListener](../../com.nishtahir.linkbait.plugin.model/-event-listener.md)): Unit` |

