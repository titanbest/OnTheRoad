package com.sergey.data.tactic

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.sergey.data.entity.EnemyUnitEntity
import com.sergey.data.tactic.BaseDao

@Dao
interface EnemyUnitDao : BaseDao<EnemyUnitEntity> {

    @Query("SELECT * FROM EnemyUnitEntity WHERE id = :id")
    fun getEnemy(id: Int): EnemyUnitEntity

    @Query("SELECT * FROM EnemyUnitEntity")
    fun getAllEnemy(): List<EnemyUnitEntity>
}