package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.plugin.MessageBuilder
import org.jetbrains.annotations.NotNull;

abstract public class AbstractMessageBuilder implements MessageBuilder {

    StringBuilder stringBuilder

    {
        stringBuilder = new StringBuilder()
    }

    @Override
    String build() {
        return stringBuilder.toString()
    }
}
