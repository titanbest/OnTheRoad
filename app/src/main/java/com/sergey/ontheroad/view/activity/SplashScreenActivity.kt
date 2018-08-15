package com.sergey.ontheroad.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        someTestFun()
    }

    private fun someTestFun() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}