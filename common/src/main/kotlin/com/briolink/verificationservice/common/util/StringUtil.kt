package com.briolink.verificationservice.common.util

object StringUtil {
    fun replaceNonWord(str: String, replaceSymbol: String = " "): String = str.replace("[^\\p{L}\\p{N}_]+", replaceSymbol)
}
