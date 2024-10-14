package com.test.task.bootcounter.main.boot

import com.test.task.bootcounter.data.source.local.db.dao.BootItemDao
import com.test.task.bootcounter.data.source.local.db.entity.BootItem
import kotlinx.coroutines.flow.Flow

class BootItemRepository (private val bootItemDao: BootItemDao) {

    fun allBootItems(): Flow<List<BootItem>> {
        return bootItemDao.allBoots()
    }

    fun insert(bootItem: BootItem) {
        bootItemDao.insert(bootItem)
    }
}