package net.nekomura.newrandomanswer

object CharUtils {
    /**
     * 是否為英文字母
     */
    fun Char.isEnglishLetter(): Boolean {
        return this in 'a'..'z' || this in 'A'..'Z'
    }
}