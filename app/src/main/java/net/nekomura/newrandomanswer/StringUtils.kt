package net.nekomura.newrandomanswer

object StringUtils {

    /**
     * 將字符串的每一個字符都放進ArrayList<Char>裡
     */
    fun String.toCharArrayList(): ArrayList<Char> {
        val array = ArrayList<Char>()
        for (i in this.indices) {
            array.add(this[i])
        }
        return array
    }

}