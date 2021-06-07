package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var downloadManager:DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setDownloadState(ButtonState.Loading)
        createNotificationChannel()

        custom_button.setOnClickListener {
            if (radioGroup3.checkedRadioButtonId == -1){
                Toast.makeText(this, "You have to click something", Toast.LENGTH_LONG).show()
            }
            else {
                custom_button.setDownloadState(ButtonState.Clicked)
                when (radioGroup3.checkedRadioButtonId) {
                    R.id.glide -> {
                        download("https://github.com/bumptech/glide", getString(R.string.glide))
                    }
                    R.id.loadapp -> {
                        download("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter", getString(R.string.loadapp))
                    }
                    R.id.retrofit -> {
                        download("https://github.com/square/retrofit", getString(R.string.retrofit))
                    }
                    else -> {
                        Toast.makeText(this, "choose one file", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "test_notification"
            val descriptionText = "test_notification_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("hello", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun basicNotification(filename: String, status: String) {
        val contentIntent = Intent(applicationContext, DetailActivity::class.java)
        contentIntent.putExtra("filename", filename)
        contentIntent.putExtra("status", status)

        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, "hello")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Udacity Kotlin Nanodegree")
            .setContentText("The Project 3 repository is downloaded.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.cloud_download, "Check Status", contentPendingIntent)
        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent!!.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor: Cursor =downloadManager.query(DownloadManager.Query().setFilterById(id!!))
            if (cursor.moveToFirst()){
                val status = cursor.getInt(
                        cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                )
                var fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        basicNotification(fileName, "Success")
                    }
                    DownloadManager.STATUS_FAILED -> {
                        basicNotification(fileName, "Failed")
                    }

                }
            }
        }
    }

    private fun download(URL:String, Name: String) {
        Toast.makeText(this, "Downloading", Toast.LENGTH_LONG).show()
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(Name)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val CHANNEL_ID = "channelId"
    }

}
