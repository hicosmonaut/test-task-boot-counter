package com.test.task.bootcounter.di

import androidx.room.Room
import androidx.work.WorkManager
import com.test.task.bootcounter.data.source.local.db.AppDatabase
import com.test.task.bootcounter.main.boot.BootItemRepository
import com.test.task.bootcounter.main.boot.BootUiItemRepository
import com.test.task.bootcounter.main.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AllModules {
    //todo: we can separate modules in future
    val commonModules = module {
        single {
            Room.databaseBuilder(
                androidApplication(),
                AppDatabase::class.java,
                "boot-database"
            ).build()
        }
        single { get<AppDatabase>().bootDao() }
        single { WorkManager.getInstance(androidApplication()) }

        single { BootUiItemRepository(get()) }
        single { BootItemRepository(get()) }
        viewModel { MainViewModel(get()) }
    }
}