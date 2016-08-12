[data](../index.md) / [com.nishtahir.linkbait.plugin](.)


## Package com.nishtahir.linkbait.plugin


### Types


| [Attachment](-attachment/index.md) | `class Attachment`
Attachment in a message.
I could not figure out how to make this
visible as an inner class.

 |
| [MessageEvent](-message-event/index.md) | `class MessageEvent&nbsp;:&nbsp;[Event](../com.nishtahir.linkbait.plugin.model/-event/index.md)`
Nice bundle of sticks put together when a
message is received.

 |
| [MessageEventListener](-message-event-listener/index.md) | `interface MessageEventListener&nbsp;:&nbsp;[EventListener](../com.nishtahir.linkbait.plugin.model/-event-listener.md)`
Receives notifications when messages are received.

 |
| [Messenger](-messenger/index.md) | `interface Messenger`
Abstract out messaging functionality such that its not dependent
on any single framework. If a specific function is not supported by
a service its expected to throw an exception.

 |
| [Plugin](-plugin/index.md) | `abstract class Plugin`
Base implementation of a plugin

 |
| [PluginContext](-plugin-context/index.md) | `interface PluginContext`
Context in which plugin is currently operating. Supplied by active bot.

 |
| [ReactionEvent](-reaction-event/index.md) | `class ReactionEvent&nbsp;:&nbsp;[Event](../com.nishtahir.linkbait.plugin.model/-event/index.md)` |
| [ReactionEventListener](-reaction-event-listener/index.md) | `interface ReactionEventListener&nbsp;:&nbsp;[EventListener](../com.nishtahir.linkbait.plugin.model/-event-listener.md)`
Receives notifications when reactions happen.

 |
| [Type](-type/index.md) | `enum class Type`
Type of Data storage

 |


### Annotations


| [Extention](-extention/index.md) | `annotation class Extention`
Annotation used to indicate that a class is a pluging

 |


### Functions


| [DataSource](-data-source.md) | `fun DataSource(type:&nbsp;[Type](-type/index.md), url:&nbsp;String, username:&nbsp;String, password:&nbsp;String): [DataSource](http://docs.oracle.com/javase/6/docs/api/javax/sql/DataSource.html)`
Factory method to provide database connection instances. This prevents a clash when
loading drivers using the Driver manager

 |

