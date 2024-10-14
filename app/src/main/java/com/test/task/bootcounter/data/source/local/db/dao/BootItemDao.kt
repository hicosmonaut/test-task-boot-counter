package com.test.task.bootcounter.data.source.local.db.dao

import androidx.room.*
import com.test.task.bootcounter.data.source.local.db.entity.BootItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BootItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bootItem: BootItem)

    @Query("SELECT * FROM boot_items")
    fun allBoots(): Flow<List<BootItem>>

}