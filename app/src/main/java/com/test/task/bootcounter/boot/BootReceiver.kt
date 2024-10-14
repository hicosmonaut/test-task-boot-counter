package com.test.task.bootcounter.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.test.task.bootcounter.data.source.local.db.entity.BootItem
import com.test.task.bootcounter.main.boot.BootItemRepository
import com.test.task.bootcounter.notification.NotificationWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {

    private val bootItemRepository: BootItemRepository by inject(BootItemRepository::class.java)
    private val workManager: WorkManager by inject(WorkManager::class.java)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            CoroutineScope(Dispatchers.IO).launch {
                bootItemRepository.insert(
                    BootItem(
                        timestamp = System.currentTimeMillis()
                    )
                )

                workManager.enqueueUniquePeriodicWork(
                    "notification_work",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    PeriodicWorkRequestBuilder<NotificationWorker>(
                        15, TimeUnit.MINUTES
                    ).build()
                )

            }

        }
    }
}