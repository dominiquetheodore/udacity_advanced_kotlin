package com.udacity

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val filename:String = intent.getStringExtra("filename").toString()
        val status:String = intent.getStringExtra("status").toString()
        NotificationManagerCompat.from(this).cancel(1);

        textView7.text = filename

        if (status == "Success"){
            textView8.setTextColor(Color.GREEN)
        }
        else {
            textView8.setTextColor(Color.RED)
        }

        textView8.text = status
    }

}
