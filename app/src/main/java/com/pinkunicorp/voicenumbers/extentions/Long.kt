package com.pinkunicorp.voicenumbers.extentions

private fun getStringValue(number: String): String? {
    return when (number) {
        "0" -> "zero"
        "1" -> "one"
        "2" -> "two"
        "3" -> "three"
        "4" -> "four"
        "5" -> "five"
        "6" -> "six"
        "7" -> "seven"
        "8" -> "eight"
        "9" -> "nine"
        "10" -> "ten"
        "11" -> "eleven"
        "12" -> "twelve"
        "13" -> "thirteen"
        "14" -> "fourteen"
        "15" -> "fifteen"
        "16" -> "sixteen"
        "17" -> "seventeen"
        "18" -> "eighteen"
        "19" -> "nineteen"
        "20" -> "twenty"
        "30" -> "thirty"
        "40" -> "fourty"
        "50" -> "fifty"
        "60" -> "sixty"
        "70" -> "seventy"
        "80" -> "eighty"
        "90" -> "ninety"
        "hundred" -> "hungred"
        "thousand" -> "thousand"
        "dot" -> "dot"
        else -> null
    }
}

private fun getOrdinalStringValue(number: String): String? {
    return when (number) {
        "0" -> "zero"
        "1" -> "first"
        "2" -> "second"
        "3" -> "third"
        "4" -> "fourth"
        "5" -> "fifth"
        "6" -> "sixth"
        "7" -> "seventh"
        "8" -> "eighth"
        "9" -> "ninth"
        "10" -> "tenth"
        "11" -> "eleventh"
        "12" -> "twelfth"
        "13" -> "thirteenth"
        "14" -> "fourteenth"
        "15" -> "fifteenth"
        "16" -> "sixteenth"
        "17" -> "seventeenth"
        "18" -> "eighteenth"
        "19" -> "nineteenth"
        "20" -> "twentieth"
        "30" -> "thirtieth"
        "40" -> "fortieth"
        "50" -> "fiftieth"
        "60" -> "sixtieth"
        "70" -> "seventieth"
        "80" -> "eightieth"
        "90" -> "ninetieth"
        "hundred" -> "hungredth"
        "thousand" -> "thousandth"
        "dot" -> "dot"
        else -> null
    }
}

fun Long.toWholeNumberString(needUseAnd: Boolean = true): String {
    if (this > 999_999L) {
        throw Exception("Number is too big")
    }
    val values = mutableListOf<String?>()
    val thousand = this / 1000
    val hundred = (this - thousand * 1000) / 100
    val tens = this % 100
    val ten = if (tens <= 20) 0 else (this - thousand * 1000 - hundred * 100) / 10
    val one = if (tens <= 20) tens else this - thousand * 1000 - hundred * 100 - ten * 10

    if (thousand > 0) {
        values.add(thousand.toWholeNumberString(false))
        values.add(getStringValue("thousand"))
    }
    if (hundred > 0) {
        values.add(getStringValue(hundred.toString()))
        values.add(getStringValue("hundred"))
    }
    if (ten > 0 || one > 0 && needUseAnd) {
        if (values.isNotEmpty()) {
            values.add("and")
        }
    }
    if (ten > 0) {
        if (one > 0) {
            values.add(getStringValue((ten * 10).toString()) + "-" + getStringValue(one.toString()))
        } else {
            values.add(getStringValue((ten * 10).toString()))
        }
    }
    if (ten == 0L && one > 0L || (this == 0L)) {
        values.add(getStringValue(one.toString()))
    }
    return values.filterNotNull().joinToString(" ")
}

fun Long.toOrdinalNumberString(): String {
    if (this > 999_999L) {
        throw Exception("Number is too big")
    }
    val values = mutableListOf<String?>()
    val thousand = this / 1000
    val hundred = (this - thousand * 1000) / 100
    val tens = this % 100
    val ten = if (tens <= 20) 0 else (this - thousand * 1000 - hundred * 100) / 10
    val one = if (tens <= 20) tens else this - thousand * 1000 - hundred * 100 - ten * 10

    if (thousand > 0) {
        values.add(thousand.toWholeNumberString(false))
        if (hundred == 0L && ten == 0L && one == 0L) {
            values.add(getOrdinalStringValue("thousand"))
        } else {
            values.add(getStringValue("thousand"))
        }
    }
    if (hundred > 0) {
        values.add(getStringValue(hundred.toString()))
        if (ten == 0L && one == 0L) {
            values.add(getOrdinalStringValue("hundred"))
        } else {
            values.add(getStringValue("hundred"))
        }
    }
    if (ten > 0 || one > 0) {
        if (values.isNotEmpty()) {
            values.add("and")
        }
    }
    if (ten > 0) {
        if (one == 0L) {
            values.add(getOrdinalStringValue((ten * 10).toString()))
        } else {
            values.add(getStringValue((ten * 10).toString()))
        }
    }
    if (one > 0 || (this == 0L)) {
        values.add(getOrdinalStringValue(one.toString()))
    }
    return values.filterNotNull().joinToString(" ")
}