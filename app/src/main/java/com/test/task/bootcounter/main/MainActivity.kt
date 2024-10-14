package com.test.task.bootcounter.main

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.task.bootcounter.R
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //todo: use view binding

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.items.collect { items ->
                    val history = StringBuilder()

                    if(items.isEmpty()){
                        history.append(
                            getString(R.string.label_no_boots_detected)
                        )
                    } else {
                        items.forEach { item ->
                            history.append(
                                getString(R.string.data_boot_date_and_count, item.date, item.count)
                            )
                                .appendLine()
                        }
                    }

                    findViewById<TextView>(R.id.main_tv_boot_history).text = history.toString()
                }
            }
        }

        findViewById<TextView>(R.id.main_btn_request_notification_permission).setOnClickListener {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PERMISSION_GRANTED){
                Toast.makeText(this, R.string.label_permission_granted, Toast.LENGTH_SHORT).show()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    Toast.makeText(this, R.string.label_permission_granted, Toast.LENGTH_SHORT).show()
                }
            }
        }

        //todo: for quick test only
        findViewById<TextView>(R.id.main_btn_test).setOnClickListener {
            mainViewModel.test()
        }

    }

    //todo: on-the-run implementation for Android 13 and 14
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        val text = if (isGranted) {
            R.string.label_permission_granted
        } else {
            R.string.label_permission_not_granted
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}

//todo: what I did - comment below
/*
    - add boot receiver
    - add notification service
    - add work manager
    - add worker which fires notification every 15 minutes
    - add room db for boot
    - add ui for boot history
    - add permission request for android 13 and 14
 */

//todo: what I want to do - comment below
/*
    - make more SOLID
    - add some separation for worker
    - add some separation for broadcast receiver
    - I had no time to check my app. Ui is worked. Notification service not tested

 */