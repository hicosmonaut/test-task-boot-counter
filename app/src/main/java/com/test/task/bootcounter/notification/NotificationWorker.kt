package com.test.task.bootcounter.notification

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.test.task.bootcounter.R
import com.test.task.bootcounter.main.boot.BootItemRepository
import org.koin.java.KoinJavaComponent.inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val bootItemRepository: BootItemRepository by inject(BootItemRepository::class.java)

    @SuppressLint("StringFormatInvalid")
    override suspend fun doWork(): Result {
        bootItemRepository.allBootItems().collect { items ->
            val contentStringResId: Int
            val contentData: String
            when {
                items.size == 1 -> {
                    contentStringResId = R.string.data_the_boot_was_detected
                    contentData = formatMillisecondsToDate(items[0].timestamp)
                }
                items.size > 1 -> {
                    contentStringResId = R.string.data_last_boots_time_delta
                    val lastTwo = items.takeLast(2)
                    contentData = formatMillisecondsToDelta(
                        lastTwo[1].timestamp - lastTwo[0].timestamp
                    )
                }
                else -> {
                    contentStringResId = R.string.label_no_boots_detected
                    contentData = ""
                }
            }

            val content = if(contentData.isEmpty()){
                appContext.getString(contentStringResId)
            } else {
                appContext.getString(contentStringResId, contentData)
            }

            val notificationServiceIntent = Intent(appContext, NotificationService::class.java)
            notificationServiceIntent.putExtra("content", content)
            ContextCompat.startForegroundService(appContext, notificationServiceIntent)
        }

        return Result.success()
    }

    private fun formatMillisecondsToDelta(milliseconds: Long): String {
        val hours = (milliseconds / (1000 * 60 * 60)) % 24
        val minutes = (milliseconds / (1000 * 60)) % 60
        val seconds = (milliseconds / 1000) % 60
        val millis = milliseconds % 1000

        return String.format(Locale.getDefault(), "%02d:%02d:%03d:%04d", hours, minutes, seconds, millis)
    }

    private fun formatMillisecondsToDate(milliseconds: Long): String {
        val date = Date(milliseconds)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
}