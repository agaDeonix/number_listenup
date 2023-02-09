package com.pinkunicorp.voicenumbers.extentions

fun Float.toRationalAndString(): String {
    val (numerator, denominator) = this.toString().split(".")
    return numerator.toLong().toWholeNumberString() +
            " and " +
            denominator.toLong().toWholeNumberString()
}

fun Float.toRationalPointString(): String {
    val (numerator, denominator) = this.toString().split(".")
    val denominatorString = denominator.map {
        it.code.toLong().toWholeNumberString()
    }.joinToString(" ")
    if (numerator == "0") {
        return " point " + denominatorString
    }
    return numerator.toLong().toWholeNumberString() + " point " + denominatorString
}