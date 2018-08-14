package com.sergey.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class EnemyUnitEntity(
        @PrimaryKey
        val id: Int,        //Id
        val name: String,   //Enemy name
        val hp: Int,        //HitPoints
        val dropGold: Int   //Drop gold of enemy
)