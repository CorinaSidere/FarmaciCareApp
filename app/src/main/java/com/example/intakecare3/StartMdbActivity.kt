package com.example.intakecare3

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.connectingtomdb.R
import com.example.intakecare3.loginphase.LogInActivity

class StartMdbActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_mdb)

        //SPLASHSCREEN
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        },2000)


        }

    }



