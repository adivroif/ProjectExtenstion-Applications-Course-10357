package com.example.myapplication55

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ToastVibrate : AppCompatActivity() {

    private lateinit var toast_background: AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toast_vibrate)
        findViews()
        initViews()
    }

    private fun initViews() {

    }

    private fun findViews() {
        toast_background = findViewById(R.id.toast_background)
    }

    fun CrashToast(context: Context,text: String) {
        Toast
            .makeText(
                context,
                text,
                Toast.LENGTH_LONG
            ).show()
    }
}
