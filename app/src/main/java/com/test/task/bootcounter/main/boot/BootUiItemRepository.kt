package com.test.task.bootcounter.main.boot

import com.test.task.bootcounter.data.source.local.db.entity.BootItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class BootUiItemRepository (private val bootItemRepository: BootItemRepository){

    fun allBootUiItems(): Flow<List<BootUiItem>> {
        return bootItemRepository.allBootItems().map { items ->

            val groupedByDate = items.groupBy { bootItem ->
                val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
                    timeInMillis = bootItem.timestamp
                }
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            }

            groupedByDate.map { (date, bootItems) ->
                BootUiItem(
                    date = date,
                    count = bootItems.size
                )
            }
        }
    }

    fun insert(bootItem: BootItem) {
        bootItemRepository.insert(bootItem)
    }
}