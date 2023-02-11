package com.pinkunicorp.numberlistenup.data.repository

import com.pinkunicorp.numberlistenup.data.model.NumberVariantState

interface SettingsRepository {
    suspend fun getNumberVariantStates(): List<NumberVariantState>
    suspend fun enableNumberVariant(variant: NumberVariantState)
    suspend fun disableNumberVariant(variant: NumberVariantState)
}