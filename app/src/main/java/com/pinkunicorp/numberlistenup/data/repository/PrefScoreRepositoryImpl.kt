package com.pinkunicorp.numberlistenup.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class PrefScoreRepositoryImpl(private val context: Context) : ScoreRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "score")

    override fun getScore() = context.dataStore.data.map { settings ->
        settings[intPreferencesKey(SCORE_KEY)] ?: 0
    }

    override suspend fun addScore(score: Int) {
        context.dataStore.edit { settings ->
            settings[intPreferencesKey(SCORE_KEY)] = (settings[intPreferencesKey(SCORE_KEY)] ?: 0) + score
        }
    }

    companion object {
        private const val SCORE_KEY = "score"
    }
}