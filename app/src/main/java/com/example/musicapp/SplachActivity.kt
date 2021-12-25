package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {
    var luncherManager: LuncherManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splach)
        luncherManager = LuncherManager(this)
        Handler().postDelayed({
            if (luncherManager!!.isFirstTime) {
                luncherManager!!.setFirstLunch(false)
                startActivity(Intent(applicationContext, WelcomeActivity::class.java))
            } else {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }, 2000)
    }
}
