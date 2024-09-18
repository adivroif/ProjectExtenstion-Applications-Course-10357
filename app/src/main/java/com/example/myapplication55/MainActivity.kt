package com.example.myapplication55

import android.content.Intent
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication55.logic.GameManager
import com.example.myapplication55.utilities.Constants
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    val rows = 5
    val cols = 3

    private lateinit var hearts : Array<ShapeableImageView>

    private lateinit var rocks : Array<ShapeableImageView>

    private lateinit var cars : Array<ShapeableImageView>

    private lateinit var runOfMatch: Job

    private lateinit var main_BTN_start_game: MaterialButton

    private lateinit var main_FAB_button_left: ExtendedFloatingActionButton

    private lateinit var main_FAB_button_right: ExtendedFloatingActionButton

    private lateinit var gameManager: GameManager

    private lateinit var toastVibrate: ToastVibrate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        gameManager = GameManager(3)
        toastVibrate = ToastVibrate()
        findViwes()
        initViwes()
    }

    fun findViwes()
    {
        main_FAB_button_left = findViewById(R.id.main_FAB_button_left)
        main_FAB_button_right = findViewById(R.id.main_FAB_button_right)
        main_BTN_start_game = findViewById(R.id.main_BTN_start_game)
        hearts = arrayOf(
            findViewById(R.id.heart0_main),
            findViewById(R.id.heart1_main),
            findViewById(R.id.heart2_main)
        )
        cars = arrayOf(
            findViewById(R.id.car_main0),
            findViewById(R.id.car_main1),
            findViewById(R.id.car_main2)
        )
        rocks = arrayOf(
            findViewById(R.id.rock0),
            findViewById(R.id.rock1),
            findViewById(R.id.rock2),
            findViewById(R.id.rock3),
            findViewById(R.id.rock4),
            findViewById(R.id.rock5),
            findViewById(R.id.rock6),
            findViewById(R.id.rock7),
            findViewById(R.id.rock8),
            findViewById(R.id.rock9),
            findViewById(R.id.rock10),
            findViewById(R.id.rock11)
        )
    }

    fun initViwes()
    {
        main_FAB_button_left.setOnClickListener { v -> moveCarLeft() }
        main_FAB_button_right.setOnClickListener { v -> moveCarRight() }
        main_BTN_start_game.setOnClickListener { v -> beginMatch() }
        refreshUI()
        for(i in hearts.indices)
            hearts[i].visibility = View.VISIBLE
        for(i in rocks.indices)
            rocks[i].visibility = View.INVISIBLE
    }

    private fun beginMatch() {
        main_BTN_start_game.visibility = View.INVISIBLE
        var lastIteration = IntArray(19)
        runOfMatch = lifecycleScope.launch {
            for(i in cars.indices) {
                if (i == 1)
                    cars[i].visibility = View.VISIBLE
                else
                    cars[i].visibility = View.INVISIBLE
            }
            while (gameManager.numberOfCrashs != gameManager.lifeCount) {

                fun setRockPositions(levelStart: Int, offset: Int) {
                    lastIteration[levelStart] = Random.nextInt(0, 3)
                    lastIteration[levelStart + 1] = lastIteration[levelStart] + offset
                    lastIteration[levelStart + 2] = lastIteration[levelStart + 1] + offset
                }

                setRockPositions(1, cols)
                setRockPositions(4, cols)
                setRockPositions(7, cols)
                setRockPositions(10, cols)
                setRockPositions(13, cols)
                setRockPositions(16, cols)

                rocks[lastIteration[1]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                moveRocks(lastIteration[1],cols)
                rocks[lastIteration[4]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                moveRocks(lastIteration[2],cols)
                moveRocks(lastIteration[4],cols)
                rocks[lastIteration[7]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                moveRocks(lastIteration[3],cols)
                moveRocks(lastIteration[5],cols)
                moveRocks(lastIteration[7],cols)
                rocks[lastIteration[10]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                checkCrash(lastIteration[3])
                moveRocks(lastIteration[6],cols)
                moveRocks(lastIteration[8],cols)
                moveRocks(lastIteration[10],cols)
                rocks[lastIteration[13]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                checkCrash(lastIteration[6])
                moveRocks(lastIteration[9],cols)
                moveRocks(lastIteration[11],cols)
                moveRocks(lastIteration[13],cols)
                rocks[lastIteration[16]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                checkCrash(lastIteration[9])
                moveRocks(lastIteration[12],cols)
                moveRocks(lastIteration[14],cols)
                moveRocks(lastIteration[16],cols)
                delay(Constants.DELAY)
                checkCrash(lastIteration[12])
                moveRocks(lastIteration[15],cols)
                moveRocks(lastIteration[17],cols)
                delay(Constants.DELAY)
                checkCrash(lastIteration[15])
                moveRocks(lastIteration[18],cols)
                delay(Constants.DELAY)
                checkCrash(lastIteration[18])
            }

        refreshUI()
        }
        runOfMatch.cancel();
    }

    private fun checkCrash(iteration : Int) {
        if ((iteration + cols) % cols == gameManager.currentIndexOfCar) { // crash
            hearts[gameManager.numberOfCrashs].visibility = View.INVISIBLE
            gameManager.numberOfCrashs += 1
            toastAndVibrate("You Crash!")
            rocks[iteration + cols].visibility = View.INVISIBLE
        }
        rocks[iteration + cols].visibility = View.INVISIBLE
        if(gameManager.numberOfCrashs == gameManager.lifeCount)
            refreshUI()
    }

    private suspend fun moveRocks(number : Int, cols :Int)
    {
        rocks[number + cols].visibility = View.VISIBLE
        rocks[number].visibility = View.INVISIBLE
    }

    private fun toastAndVibrate(text : String) {
        toastVibrate.CrashToast(this,text)
        CrashVibrate()
    }
    private fun moveCarRight() {
        gameManager.moveCarRight()
        refreshUI()
    }

    private fun moveCarLeft() {
        gameManager.moveCarLeft()
        refreshUI()
    }

    private fun refreshUI() {
        if (gameManager.isGameLost) { // Lost:
            Log.d("Game Status", "Game Over!")
            changeActivity("😭Game Over!")
        } else { // Ongoing:
            for (i in this.cars.indices)
            {
                if(i != gameManager.currentIndexOfCar)
                    cars[i].visibility = View.INVISIBLE
                else
                    cars[i].visibility = View.VISIBLE
            }
        }
    }

    private fun changeActivity(message: String) {
        val intent = Intent(this, LostActivity::class.java);
        var bundle = Bundle()
        bundle.putString(Constants.STATUS_KEY, message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    fun CrashVibrate()
    {
        val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                this.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            this.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val sosPattern = longArrayOf(
                0,
                500L
            )
            val waveVibrationEffect = VibrationEffect.createWaveform(
                sosPattern,
                -1
            )

            val oneShotVibrationEffect = VibrationEffect.createOneShot(
                500,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
            vibrator.vibrate(waveVibrationEffect)
        } else {
            vibrator.vibrate(500)
        }
    }
}