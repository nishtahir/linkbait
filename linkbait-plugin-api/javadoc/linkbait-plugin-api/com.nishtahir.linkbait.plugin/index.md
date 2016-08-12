[linkbait-plugin-api](../index.md) / [com.nishtahir.linkbait.plugin](.)

## Package com.nishtahir.linkbait.plugin

### Types

| Name | Summary |
|---|---|
| [Attachment](-attachment/index.md) | `class Attachment`<br>Attachment in a message.
I could not figure out how to make this
visible as an inner class. |
| [MessageEvent](-message-event/index.md) | `class MessageEvent : `[`Event`](../com.nishtahir.linkbait.plugin.model/-event/index.md)<br>Nice bundle of sticks put together when a
message is received. |
| [MessageEventListener](-message-event-listener/index.md) | `interface MessageEventListener : `[`EventListener`](../com.nishtahir.linkbait.plugin.model/-event-listener.md)<br>Receives notifications when messages are received. |
| [Messenger](-messenger/index.md) | `interface Messenger`<br>Abstract out messaging functionality such that its not dependent
on any single framework. If a specific function is not supported by
a service its expected to throw an exception. |
| [Plugin](-plugin/index.md) | `abstract class Plugin`<br>Base implementation of a plugin |
| [PluginContext](-plugin-context/index.md) | `interface PluginContext`<br>Context in which plugin is currently operating. Supplied by active bot. |
| [ReactionEvent](-reaction-event/index.md) | `class ReactionEvent : `[`Event`](../com.nishtahir.linkbait.plugin.model/-event/index.md) |
| [ReactionEventListener](-reaction-event-listener/index.md) | `interface ReactionEventListener : `[`EventListener`](../com.nishtahir.linkbait.plugin.model/-event-listener.md)<br>Receives notifications when reactions happen. |
| [Type](-type/index.md) | `enum class Type`<br>Type of Data storage |

### Annotations

| Name | Summary |
|---|---|
| [Extention](-extention/index.md) | `annotation class Extention`<br>Annotation used to indicate that a class is a pluging |

### Functions

| Name | Summary |
|---|---|
| [DataSource](-data-source.md) | `fun DataSource(type: `[`Type`](-type/index.md)`, url: String, username: String, password: String): `[`DataSource`](http://docs.oracle.com/javase/6/docs/api/javax/sql/DataSource.html)<br>Factory method to provide database connection instances. This prevents a clash when
loading drivers using the Driver manager |
