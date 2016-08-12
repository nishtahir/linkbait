[linkbait-plugin-api](../../index.md) / [com.nishtahir.linkbait.plugin.model](../index.md) / [Event](.)


# Event

`abstract class Event` [(source)](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugin-api/src/main/kotlin//com/nishtahir/linkbait/plugin/model/Event.kt#L7)

Base of any event. Contains a collection of
generic fields that any event should have.




### Constructors


| [&lt;init&gt;](-init-.md) | `Event()`
Base of any event. Contains a collection of
generic fields that any event should have.

 |


### Properties


| [channel](channel.md) | `var channel: String`
Channel the message originated

 |
| [id](id.md) | `var id: String`
Id of event

 |
| [isDirectedAtBot](is-directed-at-bot.md) | `var isDirectedAtBot: Boolean` |
| [sender](sender.md) | `var sender: String`
person that reacted

 |


### Inheritors


| [MessageEvent](../../com.nishtahir.linkbait.plugin/-message-event/index.md) | `class MessageEvent&nbsp;:&nbsp;Event`
Nice bundle of sticks put together when a
message is received.

 |
| [ReactionEvent](../../com.nishtahir.linkbait.plugin/-reaction-event/index.md) | `class ReactionEvent&nbsp;:&nbsp;Event` |

