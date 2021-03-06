package com.sergey.data.tactic

import android.arch.persistence.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insert(vararg insert: T): Array<Long>

    @Update fun update(vararg update: T): Int

    @Delete fun delete(vararg delete: T)
}