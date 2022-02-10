package com.udacity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(tool_bar)
        val fileName = intent.extras?.getString(MainActivity.SELECTED_FILE,"")
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)
        download_status.text = resources.getString(R.string.download_text)
        download_path.text = fileName
        motion_layout.transitionToStart()

        custom_button.setOnClickListener {
            motion_layout.transitionToEnd()
               finish()
        }
    }
}
