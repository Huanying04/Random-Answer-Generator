package net.nekomura.newrandomanswer

class StringUtils {

    fun toCharArrayList(string: String): ArrayList<Char> {
        val array = ArrayList<Char>()
        for (i in string.indices) {
            array.add(string[i])
        }
        return array
    }

}