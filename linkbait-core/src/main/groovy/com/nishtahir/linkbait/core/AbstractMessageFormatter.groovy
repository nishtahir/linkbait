package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.plugin.MessageFormatter
import org.jetbrains.annotations.NotNull;

abstract public class AbstractMessageFormatter implements MessageFormatter{

    StringBuilder stringBuilder

    public String format(Closure closure) {
        stringBuilder = new StringBuilder()
        runClosure(closure)
        return stringBuilder.toString()
    }

    abstract void par(@NotNull String text)

    abstract void bold(@NotNull String text)

    abstract void italics(@NotNull String text)

    abstract void strike(@NotNull String text)

    abstract void pre(@NotNull String text)

    abstract void code(@NotNull String text)

    abstract void emoji(@NotNull String text)

    abstract void link(@NotNull String title, @NotNull String url)


    void clear() {
        stringBuilder = new StringBuilder()
    }

    private runClosure(Closure runClosure) {
        // Create clone of closure for threading access.
        Closure runClone = (Closure) runClosure.clone()

        // Set delegate of closure to this builder.
        runClone.delegate = this

        // And only use this builder as the closure delegate.
        runClone.resolveStrategy = Closure.DELEGATE_ONLY

        // Run closure code.
        runClone()
    }

}
