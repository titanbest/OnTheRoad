package com.sergey.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class GameProcessEntity(
        @PrimaryKey
        val processId: Int,
        val lvl: Int,       //State on level now
        val subLvl: Int,    //State on sub-level now
        val allGold: Int    //Cumulative gold
)