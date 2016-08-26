package com.nishtahir.linkbait.core.slack

import com.nishtahir.linkbait.core.AbstractMessageBuilder
import com.nishtahir.linkbait.plugin.MessageBuilder
import org.jetbrains.annotations.NotNull

class SlackMessageBuilder extends AbstractMessageBuilder {
    
    @Override
    MessageBuilder text(@NotNull String text) {
        stringBuilder.append(text)
        return this
    }

    @Override
    MessageBuilder bold(@NotNull String text) {
        stringBuilder.append("*${text}*")
        return this
    }

    @Override
    MessageBuilder italics(@NotNull String text) {
        stringBuilder.append("_${text}_")
        return this
    }

    @Override
    MessageBuilder strike(@NotNull String text) {
        stringBuilder.append("~${text}~")
        return this
    }

    @Override
    MessageBuilder pre(@NotNull String text) {
        stringBuilder.append("```${text}```")
        return this
    }

    @Override
    MessageBuilder code(@NotNull String text) {
        stringBuilder.append("`${text}`")
        return this
    }

    @Override
    MessageBuilder emoji(@NotNull String text) {
        stringBuilder.append(":${text}:")
        return this
    }

    @Override
    MessageBuilder link(@NotNull String title, @NotNull String url) {
        return this
    }

}
