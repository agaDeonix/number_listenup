package com.pinkunicorp.numberlistenup.data.repository

import kotlinx.coroutines.flow.Flow

interface ScoreRepository {
    fun getScore(): Flow<Int>
    suspend fun addScore(score: Int)
}