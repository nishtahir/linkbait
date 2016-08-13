package com.nishtahir.linkbait.heysnackfood

fun StringBuilder.appendNew(str: String) : StringBuilder {
    this.append(str + '\n')
    return this
}