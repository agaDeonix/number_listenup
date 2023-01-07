package com.pinkunicorp.voicenumbers.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.pinkunicorp.voicenumbers.data.model.NumberType
import com.pinkunicorp.voicenumbers.data.model.NumberVariantState

class PrefSettingsRepositoryImpl(val context: Context) : SettingsRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override suspend fun getNumberVariantStates(): List<NumberVariantState> {
        val result = mutableListOf<NumberVariantState>()
        context.dataStore.edit { settings ->
            NumberType.values().forEach { type ->
                result.add(
                    NumberVariantState(
                        type,
                        settings[booleanPreferencesKey(type.name)] ?: true
                    )
                )
            }
        }
        return result
    }

    override suspend fun enableNumberVariant(variant: NumberVariantState) {
        context.dataStore.edit { settings ->
            settings[booleanPreferencesKey(variant.type.name)] = true
        }
    }

    override suspend fun disableNumberVariant(variant: NumberVariantState) {
        context.dataStore.edit { settings ->
            settings[booleanPreferencesKey(variant.type.name)] = false
        }
    }
}