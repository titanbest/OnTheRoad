package com.sergey.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.sergey.data.entity.EnemyUnitEntity
import com.sergey.data.entity.GameProcessEntity
import com.sergey.data.tactic.EnemyUnitDao
import com.sergey.data.tactic.GameProcessDao
import com.sergey.data.utils.DataBaseUtils

@Database(
        entities = [EnemyUnitEntity::class, GameProcessEntity::class],
        version = DataBaseUtils.VERSION,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun enemyDao(): EnemyUnitDao

    abstract fun gameProcessDao(): GameProcessDao

}