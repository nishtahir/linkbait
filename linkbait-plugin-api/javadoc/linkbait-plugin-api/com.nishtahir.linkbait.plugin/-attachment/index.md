[data](../../index.md) / [com.nishtahir.linkbait.plugin](../index.md) / [Attachment](.)


# Attachment

`class Attachment` [(source)](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugin-api/src/main/kotlin//com/nishtahir/linkbait/plugin/Attachment.kt#L13)

Attachment in a message.
I could not figure out how to make this
visible as an inner class.


If you figure it out please fix and let me know


Nish






### Constructors


| [&lt;init&gt;](-init-.md) | `Attachment(title:&nbsp;String, body:&nbsp;String, color:&nbsp;String, titleUrl:&nbsp;String&nbsp;=&nbsp;"", thumbnailUrl:&nbsp;String&nbsp;=&nbsp;"", imageUrl:&nbsp;String&nbsp;=&nbsp;"")`
Attachment in a message.
I could not figure out how to make this
visible as an inner class.

 |


### Properties


| [additionalFields](additional-fields.md) | `var additionalFields: Map&lt;String,&nbsp;String&gt;?`
Additional content in the attachment.

 |
| [body](body.md) | `val body: String`
Main content of the Attachment.

 |
| [color](color.md) | `val color: String`
Used if the service supports colors.

 |
| [imageUrl](image-url.md) | `val imageUrl: String`
Adds an image to the attachment.

 |
| [thumbnailUrl](thumbnail-url.md) | `val thumbnailUrl: String`
Adds a thumbnail to the attachment.

 |
| [title](title.md) | `val title: String`
Title of Attachment.

 |
| [titleUrl](title-url.md) | `val titleUrl: String`
Makes the title of the attachment a clickable link.

 |

