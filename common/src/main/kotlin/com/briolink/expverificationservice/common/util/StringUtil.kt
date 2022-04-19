package com.briolink.expverificationservice.common.util

object StringUtil {
    fun replaceNonWord(str: String, replaceSymbol: String = " "): String = str.replace("[^\\p{L}\\p{N}_]+", replaceSymbol)
}
