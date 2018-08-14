package com.sergey.data.tactic

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.sergey.data.entity.GameProcessEntity
import com.sergey.data.tactic.BaseDao

@Dao
interface GameProcessDao : BaseDao<GameProcessEntity> {

    @Query("SELECT * FROM GameProcessEntity")
    fun getStatsProcessGame(): GameProcessEntity
}