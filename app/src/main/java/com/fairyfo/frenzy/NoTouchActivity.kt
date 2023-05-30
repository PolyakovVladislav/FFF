package com.fairyfo.frenzy

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.contains
import com.fairyfo.frenzy.ui.fragments.game2.Game2ViewModel
import com.fairyfo.frenzy.ui.view.FrenzyProgressBar
import com.onesignal.OneSignal
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

@Obfuscate
@SuppressLint("CustomSplashScreen")
class NoTouchActivity : AppCompatActivity() {
    private val uiScope = CoroutineScope(Dispatchers.Main + Job())
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        val progressBar = findViewById<FrenzyProgressBar>(R.id.progress_bar)
        progressBar.clicksEnabled = false
        uiScope.launch {
            progressBar.progress = 0
            do {
                delay(300)
                progressBar.progress += 100 / 8
            } while (progressBar.progress < 100)
            toMainActivity()
        }
    }

    override fun onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        }

        super.onDestroy()
    }


    private fun toMainActivity() {
        ioScope.launch {
            delay(3000) 
            uiScope.launch {
                startActivity(Intent(this@NoTouchActivity, OpenActivity::class.java))
                finish()
            }
        }
    }
}