package com.udacity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    var selectedOption = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(tool_bar)
        registerReceiver(bcReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button.setOnClickListener {

            when {
                glide_radio_button.isChecked -> {
                    selectedOption = resources.getString(R.string.glide_text)
                    download("https://github.com/bumptech/glide")
                    custom_button.setLoadingState(ButtonState.Clicked)
                }
                udacity_radio_button.isChecked -> {
                    selectedOption = resources.getString(R.string.repository_text)
                    download(URL)
                    custom_button.setLoadingState(ButtonState.Clicked)
                }
                retrofit_radio_button.isChecked -> {
                    selectedOption = resources.getString(R.string.retrofit_text)
                    download("https://github.com/square/retrofit")
                    custom_button.setLoadingState(ButtonState.Clicked)
                }
                else -> {
                    custom_button.setLoadingState(ButtonState.Clicked)
                    Toast.makeText(
                        this@MainActivity,
                        resources.getString(R.string.select_repository),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private val bcReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val idDownload = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            sendNotification(this@MainActivity, idDownload, selectedOption)
            custom_button.setLoadingState(ButtonState.Completed)
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
    }

    companion object {
        const val DOWNLOAD_ID = "DOWNLOAD_ID"
        const val SELECTED_FILE = "SELECTED_FILE"
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        const val CHANNEL_ID = "channelId"
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bcReceiver)
    }
}
