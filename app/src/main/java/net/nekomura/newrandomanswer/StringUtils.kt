package net.nekomura.newrandomanswer

object StringUtils {

    fun toCharArrayList(string: String): ArrayList<Char> {
        val array = ArrayList<Char>()
        for (i in string.indices) {
            array.add(string[i])
        }
        return array
    }

}