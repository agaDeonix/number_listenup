package com.pinkunicorp.numberlistenup.data.model

data class NumberVariantState(val type: NumberType, val isEnable: Boolean)

enum class NumberType {
    WHOLE,
    ORDINAL,
    RATIONAL,
    FRACTION,
    TIME
}