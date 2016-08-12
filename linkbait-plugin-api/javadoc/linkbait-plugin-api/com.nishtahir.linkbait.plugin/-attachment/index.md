[linkbait-plugin-api](../../index.md) / [com.nishtahir.linkbait.plugin](../index.md) / [Attachment](.)

# Attachment

`class Attachment` [(source)](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugin-api/src/main/kotlin//com/nishtahir/linkbait/plugin/Attachment.kt#L13)

Attachment in a message.
I could not figure out how to make this
visible as an inner class.

If you figure it out please fix and let me know

Nish

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Attachment(title: String, body: String, color: String, titleUrl: String = "", thumbnailUrl: String = "", imageUrl: String = "")`<br>Attachment in a message.
I could not figure out how to make this
visible as an inner class. |

### Properties

| Name | Summary |
|---|---|
| [additionalFields](additional-fields.md) | `var additionalFields: Map<String, String>?`<br>Additional content in the attachment. |
| [body](body.md) | `val body: String`<br>Main content of the Attachment. |
| [color](color.md) | `val color: String`<br>Used if the service supports colors. |
| [imageUrl](image-url.md) | `val imageUrl: String`<br>Adds an image to the attachment. |
| [thumbnailUrl](thumbnail-url.md) | `val thumbnailUrl: String`<br>Adds a thumbnail to the attachment. |
| [title](title.md) | `val title: String`<br>Title of Attachment. |
| [titleUrl](title-url.md) | `val titleUrl: String`<br>Makes the title of the attachment a clickable link. |
