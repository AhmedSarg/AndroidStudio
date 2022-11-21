package com.udacity

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        name_text.text = MainActivity.name
        status_text.text = MainActivity.status

        val mainIntent = Intent(applicationContext, MainActivity::class.java)

        back_button.setOnClickListener {
            applicationContext.startActivity(mainIntent)
        }
    }
}
