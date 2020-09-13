package net.nekomura.newrandomanswer

class CharUtils {
    /**
     * 是否為英文字母
     * @param char 要檢查的字符
     * @return 是否為英文字母
     */
    fun isEnglishLetter(char: Char): Boolean {
        return char in 'a'..'z' || char in 'A'..'Z'
    }
}