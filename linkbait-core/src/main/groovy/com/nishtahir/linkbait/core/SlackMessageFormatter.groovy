package com.nishtahir.linkbait.core

class SlackMessageFormatter extends AbstractMessageFormatter {

    @Override
    void par(String text) {
        stringBuilder.append(text)
    }

    @Override
    void bold(String text) {
        stringBuilder.append("*${text}*")
    }

    @Override
    void italics(String text) {
        stringBuilder.append("_${text}_")
    }

    @Override
    void strike(String text) {
        stringBuilder.append("~${text}~")
    }

    @Override
    void pre(String text) {
        stringBuilder.append("```${text}```")
    }

    @Override
    void code(String text) {
        stringBuilder.append("`${text}`")
    }

    @Override
    void emoji(String text) {
        stringBuilder.append(":${text}:")
    }

    @Override
    void link(String title, String url) {

    }

    @Override
    String format() {
        return null
    }
}
