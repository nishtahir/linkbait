package com.nishtahir.linkbait.commons

import java.nio.charset.Charset

/**
 * Random Human friendly random name generator. Inspired by
 * https://github.com/kohsuke/wordnet-random-name/
 */
class RandomNameGenerator(val seed: Int = System.currentTimeMillis().toInt()) {
    private var pos: Int = seed

    @Synchronized fun next(): String {
        pos = Math.abs(pos + Dictionary.prime) % Dictionary.size()
        return Dictionary.word(pos)
    }
}

object Dictionary {
    lateinit var nouns: List<String>
    lateinit var adjectives: List<String>
    /**
     * Sufficiently big prime that's bigger than {@link #size()}
     */
    var prime: Int = 0

    init {
        adjectives = loadResource("adjectives.txt")
        nouns = loadResource("nouns.txt")

        var primeCombo = 2
        while (primeCombo <= size()) {
            val nextPrime = primeCombo + 1
            primeCombo *= nextPrime
        }
        prime = primeCombo + 1
    }

    fun size() = nouns.size * adjectives.size

    fun word(i: Int): String {
        return adjectives[i % adjectives.size].capitalize() +
                nouns[i / adjectives.size].capitalize()
    }

    private fun loadResource(name: String): List<String> {
        return this.javaClass.classLoader.getResourceAsStream(name)
                .bufferedReader(Charset.defaultCharset())
                .readLines()
    }
}