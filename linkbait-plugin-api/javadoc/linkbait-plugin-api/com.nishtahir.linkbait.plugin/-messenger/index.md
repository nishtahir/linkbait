[linkbait-plugin-api](../../index.md) / [com.nishtahir.linkbait.plugin](../index.md) / [Messenger](.)

# Messenger

`interface Messenger` [(source)](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugin-api/src/main/kotlin//com/nishtahir/linkbait/plugin/Messaging.kt#L10)

Abstract out messaging functionality such that its not dependent
on any single framework. If a specific function is not supported by
a service its expected to throw an exception.

### Functions

| Name | Summary |
|---|---|
| [addReaction](add-reaction.md) | `abstract fun addReaction(channel: String, messageId: String, reaction: String): Unit` |
| [removeReaction](remove-reaction.md) | `abstract fun removeReaction(channel: String, messageId: String, reaction: String): Unit`<br>Removes all reaction videos on YouTube. Works 100%.
They respawn pretty fast so they might be back by the time you check. |
| [sendAttachment](send-attachment.md) | `abstract fun sendAttachment(channel: String, attachment: `[`Attachment`](../-attachment/index.md)`): Unit` |
| [sendMessage](send-message.md) | `abstract fun sendMessage(channel: String, message: String): Unit` |
| [uploadFile](upload-file.md) | `abstract fun uploadFile(channel: String, file: `[`File`](http://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Unit`<br>Upload a what to a where |
