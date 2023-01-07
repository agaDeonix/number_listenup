package com.pinkunicorp.voicenumbers.data.repository

import com.pinkunicorp.voicenumbers.data.model.NumberVariantState

interface SettingsRepository {
    suspend fun getNumberVariantStates(): List<NumberVariantState>
    suspend fun enableNumberVariant(variant: NumberVariantState)
    suspend fun disableNumberVariant(variant: NumberVariantState)
}