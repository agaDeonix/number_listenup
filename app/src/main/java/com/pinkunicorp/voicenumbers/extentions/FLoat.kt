package com.pinkunicorp.voicenumbers.extentions

fun Float.toRationalString(): String {
    val (numerator, denominator) = this.toString().split(".")
    return numerator.toLong().toWholeNumberString() +
            " point " +
            denominator.toLong().toWholeNumberString()
}