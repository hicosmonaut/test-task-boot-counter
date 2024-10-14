package com.test.task.bootcounter.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.task.bootcounter.data.source.local.db.dao.BootItemDao
import com.test.task.bootcounter.data.source.local.db.entity.BootItem

@Database(entities = [BootItem::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bootDao(): BootItemDao
}