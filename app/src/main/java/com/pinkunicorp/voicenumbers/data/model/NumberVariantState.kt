package com.pinkunicorp.voicenumbers.data.model

data class NumberVariantState(val type: NumberType, val isEnable: Boolean)

enum class NumberType {
    WHOLE,
    ORDINAL,
    RATIONAL,
    FRACTION,
    DATE
}