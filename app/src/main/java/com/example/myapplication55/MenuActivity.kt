package com.example.myapplication55

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication55.interfaces.TiltCallBack
import com.example.myapplication55.utilities.Constants
import com.example.myapplication55.utilities.MoveDetector
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MenuActivity : AppCompatActivity() {

    private lateinit var moveDetector: MoveDetector

    private var sensorMode: Boolean = false

    private lateinit var sound : MediaPlayer

    private lateinit var main_sensor_mode : MaterialButton

    private lateinit var main_button_mode : MaterialButton

    private lateinit var main_fast_mode : MaterialButton

    private lateinit var main_slow_mode : MaterialButton

    private lateinit var go_to_high_score_table : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        sound = MediaPlayer.create(this,R.raw.nasheed_islamic_background)
        sound.isLooping=true
        sound.setVolume(300f,300f)
        sound.start()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        enableEdgeToEdge()
        findViwes()
        initViwes()
    }

    private fun initViwes() {
        main_sensor_mode.setOnClickListener{v -> chooseSensorMode()}
        main_button_mode.setOnClickListener{v -> chooseButtonMode()}
        main_fast_mode.setOnClickListener{v -> chooseFastMode()}
        main_slow_mode.setOnClickListener{v -> chooseSlowMode()}
        go_to_high_score_table.setOnClickListener{v-> chooseGoToHighScoreTable()}
        main_button_mode.visibility = View.VISIBLE
        main_sensor_mode.visibility = View.VISIBLE
        main_fast_mode.visibility = View.INVISIBLE
        main_slow_mode.visibility = View.INVISIBLE
        go_to_high_score_table.visibility = View.VISIBLE
    }

    private fun chooseGoToHighScoreTable() {
        sound.stop()
        main_button_mode.visibility = View.INVISIBLE
        main_sensor_mode.visibility = View.INVISIBLE
        go_to_high_score_table.visibility = View.INVISIBLE
        changeToHighScoreActivity()
    }

    private fun changeToHighScoreActivity() {
        sound.stop()
        val intent = Intent(this, HighScoreAndMapActivity::class.java);
        var bundle = Bundle()
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun chooseSensorMode() {
        sensorMode = true
        changeToMainActivity()
    }

    private fun chooseButtonMode() {
        main_button_mode.visibility = View.INVISIBLE
        main_sensor_mode.visibility = View.INVISIBLE
        go_to_high_score_table.visibility = View.INVISIBLE
        main_fast_mode.visibility = View.VISIBLE
        main_slow_mode.visibility = View.VISIBLE
    }

    private fun chooseFastMode() {
        Constants.DELAY = 750L
        main_fast_mode.visibility = View.INVISIBLE
        main_slow_mode.visibility = View.INVISIBLE
        changeToMainActivity()
    }
    private fun chooseSlowMode() {
        Constants.DELAY = 1100L
        main_fast_mode.visibility = View.INVISIBLE
        main_slow_mode.visibility = View.INVISIBLE
        changeToMainActivity()
    }

    fun findViwes() {
        main_sensor_mode = findViewById(R.id.main_sensor_mode)
        main_button_mode = findViewById(R.id.main_button_mode)
        main_fast_mode = findViewById(R.id.main_fast_mode)
        main_slow_mode = findViewById(R.id.main_slow_mode)
        go_to_high_score_table = findViewById(R.id.go_to_high_score_table)
    }

    private fun changeToMainActivity() {
        sound.stop()
        val intent = Intent(this, MainActivity::class.java)
        var bundle = Bundle()
        intent.putExtras(bundle)
        intent.putExtra("sensor_mode", sensorMode);
        startActivity(intent)
        finish()
    }
}