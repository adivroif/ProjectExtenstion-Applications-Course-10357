package com.example.myapplication55

import android.content.Intent
import android.media.MediaPlayer
import com.google.gson.Gson
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import com.example.myapplication55.interfaces.TiltCallBack
import com.example.myapplication55.logic.GameManager
import com.example.myapplication55.logic.SingleSoundPlayer
import com.example.myapplication55.models.Player
import com.example.myapplication55.utilities.Constants
import com.example.myapplication55.utilities.DataManager
import com.example.myapplication55.utilities.LocationManager
import com.example.myapplication55.utilities.MoveDetector
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    val cols = 5

    private var sensorMode: Boolean = false

    private lateinit var moveDetector: MoveDetector

    private lateinit var hearts : Array<ShapeableImageView>

    private lateinit var location : LocationManager

    private lateinit var main_player_name : AppCompatEditText

    private lateinit var main_submit : MaterialButton

    private lateinit var rocks : Array<ShapeableImageView>

    private lateinit var coins : Array<ShapeableImageView>

    private lateinit var cars : Array<ShapeableImageView>

    private lateinit var runOfMatch: Job

    private lateinit var singleSoundPlayer: SingleSoundPlayer

    private lateinit var sound : MediaPlayer

    //private lateinit var dataManager : DataManager

    private lateinit var main_BTN_start_game: MaterialButton

    private lateinit var backgroundMainImageView : ShapeableImageView

    private lateinit var main_score:MaterialTextView

    private lateinit var main_FAB_button_left: ExtendedFloatingActionButton

    private lateinit var main_FAB_button_right: ExtendedFloatingActionButton

    private lateinit var gameManager: GameManager

    private lateinit var toastVibrate: ToastVibrate

    private lateinit var listOfPlayers : ArrayList<Player>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //dataManager = DataManager(this)
        listOfPlayers = DataManager(this).readSharedPreferences(this)
        sound = MediaPlayer.create(this,R.raw.sport_epic)
        sound.isLooping=true
        sound.setVolume(300f,300f)
        sound.start()
        sensorMode = getIntent().getBooleanExtra("sensor_mode",false)
        singleSoundPlayer = SingleSoundPlayer(this)
        setContentView(R.layout.activity_main)
        gameManager = GameManager(3)
        toastVibrate = ToastVibrate()
        listOfPlayers = ArrayList<Player>()
        location = LocationManager(this)
        location.findLocation()
        findViwes()
        initViwes()
    }

    fun findViwes()
    {
        main_player_name = findViewById(R.id.main_player_name)
        main_submit = findViewById(R.id.main_submit)
        main_FAB_button_left = findViewById(R.id.main_FAB_button_left)
        main_FAB_button_right = findViewById(R.id.main_FAB_button_right)
        main_BTN_start_game = findViewById(R.id.main_BTN_start_game)
        main_score = findViewById(R.id.main_score)
        backgroundMainImageView = findViewById(R.id.backgroundMainImageView)
        hearts = arrayOf(
            findViewById(R.id.heart0_main),
            findViewById(R.id.heart1_main),
            findViewById(R.id.heart2_main)
        )
        cars = arrayOf(
            findViewById(R.id.car_main0),
            findViewById(R.id.car_main1),
            findViewById(R.id.car_main2),
            findViewById(R.id.car_main3),
            findViewById(R.id.car_main4)
        )
        rocks = arrayOf(
            findViewById(R.id.rock00),
            findViewById(R.id.rock01),
            findViewById(R.id.rock02),
            findViewById(R.id.rock03),
            findViewById(R.id.rock04),
            findViewById(R.id.rock10),
            findViewById(R.id.rock11),
            findViewById(R.id.rock12),
            findViewById(R.id.rock13),
            findViewById(R.id.rock14),
            findViewById(R.id.rock20),
            findViewById(R.id.rock21),
            findViewById(R.id.rock22),
            findViewById(R.id.rock23),
            findViewById(R.id.rock24),
            findViewById(R.id.rock30),
            findViewById(R.id.rock31),
            findViewById(R.id.rock32),
            findViewById(R.id.rock33),
            findViewById(R.id.rock34)
        )

        coins = arrayOf(
            findViewById(R.id.coin00),
            findViewById(R.id.coin01),
            findViewById(R.id.coin02),
            findViewById(R.id.coin03),
            findViewById(R.id.coin04),
            findViewById(R.id.coin10),
            findViewById(R.id.coin11),
            findViewById(R.id.coin12),
            findViewById(R.id.coin13),
            findViewById(R.id.coin14),
            findViewById(R.id.coin20),
            findViewById(R.id.coin21),
            findViewById(R.id.coin22),
            findViewById(R.id.coin23),
            findViewById(R.id.coin24),
            findViewById(R.id.coin30),
            findViewById(R.id.coin31),
            findViewById(R.id.coin32),
            findViewById(R.id.coin33),
            findViewById(R.id.coin34)
        )
    }

    fun initViwes() {
        main_player_name.visibility = View.INVISIBLE
        main_submit.visibility = View.INVISIBLE
        if (!sensorMode) {
            main_FAB_button_left.setOnClickListener { v -> moveCarLeft() }
            main_FAB_button_right.setOnClickListener { v -> moveCarRight() }
        }

        main_submit.setOnClickListener { v -> changeActivity() }
        main_BTN_start_game.setOnClickListener { v -> beginMatch() }
        refreshUI()
        for (i in hearts.indices)
            hearts[i].visibility = View.VISIBLE
        for (i in rocks.indices)
            rocks[i].visibility = View.INVISIBLE
        for (i in coins.indices)
            coins[i].visibility = View.INVISIBLE

        moveDetector = MoveDetector(this, object : TiltCallBack {

            override fun x(x1: String) {
                if (x1.equals("Left"))
                    moveCarLeft()
                else
                    moveCarRight()
            }

            override fun speed(speed: String) {
                if (speed.equals("Slow"))
                    Constants.DELAY = 1100L
                else
                    Constants.DELAY = 750L
            }
        })
        if (sensorMode) {
            main_FAB_button_right.visibility = View.GONE
            main_FAB_button_left.visibility = View.GONE
            moveDetector.startListening()
        }
    }

    private fun beginMatch() {
        main_BTN_start_game.visibility = View.INVISIBLE
        main_score.text = buildString {
            append(gameManager.score)
        }
        var lastIterationForRocks = IntArray(19)
        var lastIterationForCoins = IntArray(19)
        runOfMatch = lifecycleScope.launch {
            for(i in cars.indices) {
                if (i == gameManager.startIndexOfCar)
                    cars[i].visibility = View.VISIBLE
                else
                    cars[i].visibility = View.INVISIBLE
            }
            while (gameManager.numberOfCrashs != gameManager.lifeCount) {

                fun setRockPositions(levelStart: Int, offset: Int) {
                    lastIterationForRocks[levelStart] = Random.nextInt(0, 5)
                    lastIterationForRocks[levelStart + 1] = lastIterationForRocks[levelStart] + offset
                    lastIterationForRocks[levelStart + 2] = lastIterationForRocks[levelStart + 1] + offset
                    lastIterationForCoins[levelStart] = Random.nextInt(0, 5)
                    lastIterationForCoins[levelStart + 1] = lastIterationForCoins[levelStart] + offset
                    lastIterationForCoins[levelStart + 2] = lastIterationForCoins[levelStart + 1] + offset
                }

                setRockPositions(1, cols)
                setRockPositions(4, cols)
                setRockPositions(7, cols)
                setRockPositions(10, cols)
                setRockPositions(13, cols)
                setRockPositions(16, cols)

                rocks[lastIterationForRocks[1]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                updatdScore()
                coins[lastIterationForCoins[1]].visibility = View.VISIBLE
                moveRocks(lastIterationForRocks[1],cols)
                rocks[lastIterationForRocks[4]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                updatdScore()
                moveCoins(lastIterationForCoins[1],cols)
                coins[lastIterationForCoins[4]].visibility = View.VISIBLE
                moveRocks(lastIterationForRocks[2],cols)
                moveRocks(lastIterationForRocks[4],cols)
                rocks[lastIterationForRocks[7]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                updatdScore()
                moveCoins(lastIterationForCoins[2],cols)
                moveCoins(lastIterationForCoins[4],cols)
                coins[lastIterationForCoins[7]].visibility = View.VISIBLE
                moveRocks(lastIterationForRocks[3],cols)
                moveRocks(lastIterationForRocks[5],cols)
                moveRocks(lastIterationForRocks[7],cols)
                rocks[lastIterationForRocks[10]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                updatdScore()
                moveCoins(lastIterationForCoins[3],cols)
                moveCoins(lastIterationForCoins[5],cols)
                moveCoins(lastIterationForCoins[7],cols)
                coins[lastIterationForCoins[10]].visibility = View.VISIBLE
                checkCrashRock(lastIterationForRocks[3])
                moveRocks(lastIterationForRocks[6],cols)
                moveRocks(lastIterationForRocks[8],cols)
                moveRocks(lastIterationForRocks[10],cols)
                rocks[lastIterationForRocks[13]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                updatdScore()
                checkCrashCoin(lastIterationForCoins[3])
                moveCoins(lastIterationForCoins[6],cols)
                moveCoins(lastIterationForCoins[8],cols)
                moveCoins(lastIterationForCoins[10],cols)
                coins[lastIterationForCoins[13]].visibility = View.VISIBLE
                checkCrashRock(lastIterationForRocks[6])
                moveRocks(lastIterationForRocks[9],cols)
                moveRocks(lastIterationForRocks[11],cols)
                moveRocks(lastIterationForRocks[13],cols)
                rocks[lastIterationForRocks[16]].visibility = View.VISIBLE
                delay(Constants.DELAY)
                updatdScore()
                checkCrashCoin(lastIterationForCoins[6])
                moveCoins(lastIterationForCoins[9],cols)
                moveCoins(lastIterationForCoins[11],cols)
                moveCoins(lastIterationForCoins[13],cols)
                coins[lastIterationForCoins[16]].visibility = View.VISIBLE
                checkCrashRock(lastIterationForRocks[9])
                moveRocks(lastIterationForRocks[12],cols)
                moveRocks(lastIterationForRocks[14],cols)
                moveRocks(lastIterationForRocks[16],cols)
                delay(Constants.DELAY)
                updatdScore()
                checkCrashCoin(lastIterationForCoins[9])
                moveCoins(lastIterationForCoins[12],cols)
                moveCoins(lastIterationForCoins[14],cols)
                moveCoins(lastIterationForCoins[16],cols)
                checkCrashRock(lastIterationForRocks[12])
                moveRocks(lastIterationForRocks[15],cols)
                moveRocks(lastIterationForRocks[17],cols)
                delay(Constants.DELAY)
                updatdScore()
                checkCrashCoin(lastIterationForCoins[12])
                moveCoins(lastIterationForCoins[15],cols)
                moveCoins(lastIterationForCoins[17],cols)
                checkCrashRock(lastIterationForRocks[15])
                moveRocks(lastIterationForRocks[18],cols)
                delay(Constants.DELAY)
                updatdScore()
                checkCrashCoin(lastIterationForCoins[15])
                moveCoins(lastIterationForCoins[18],cols)
                checkCrashRock(lastIterationForRocks[18])
                delay(Constants.DELAY)
                updatdScore()
                checkCrashCoin(lastIterationForCoins[18])
            }

        }
    }

    private fun saveSharePrefrences() : Intent {
        val gson = Gson()
        var player : Player = Player(location.getLat(),location.getLng(),gameManager.score,main_player_name.getText().toString())
        listOfPlayers.add(player)
        DataManager(this).addPlayer(player)
        listOfPlayers = DataManager(this).getListOfPlayers()
        listOfPlayers.sortByDescending { it.score }
        val json = gson.toJson(listOfPlayers)
        DataManager(this).saveString(this,"list",json)
        Log.d("save", "Array is : " + json)
        var intent:Intent = Intent(this,HighScoreAndMapActivity::class.java)
        intent.putExtra("list",json)
        return intent
    }

    private fun checkCrashRock(iteration : Int) {
        if ((iteration + cols) % cols == gameManager.currentIndexOfCar) { // crash
            singleSoundPlayer.playSound(R.raw.crash)
            hearts[gameManager.numberOfCrashs].visibility = View.INVISIBLE
            gameManager.numberOfCrashs += 1
            toastAndVibrate("You Crash!")
            rocks[iteration + cols].visibility = View.INVISIBLE
        }
        rocks[iteration + cols].visibility = View.INVISIBLE
        if(gameManager.numberOfCrashs == gameManager.lifeCount) {
            runOfMatch.cancel()
            main_player_name.visibility = View.VISIBLE
            main_submit.visibility = View.VISIBLE
        }
    }

    fun updatdScore(){
        gameManager.score+=1
        main_score.text = buildString {
            append(gameManager.score);
        }
    }

    private fun checkCrashCoin(iteration : Int) {
        if ((iteration + cols) % cols == gameManager.currentIndexOfCar) { // crash
            singleSoundPlayer.playSound(R.raw.snd_ding)
            gameManager.addScore()
            toastAndVibrate("Plus one point")
            coins[iteration + cols].visibility = View.INVISIBLE
            main_score.text = buildString {
                append(gameManager.score)
            }
        }
        coins[iteration + cols].visibility = View.INVISIBLE
    }


    private suspend fun moveRocks(number : Int, cols :Int)
    {
        rocks[number + cols].visibility = View.VISIBLE
        rocks[number].visibility = View.INVISIBLE
    }

    private suspend fun moveCoins(number : Int, cols :Int)
    {
        coins[number + cols].visibility = View.VISIBLE
        coins[number].visibility = View.INVISIBLE
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
            for (i in this.cars.indices)
            {
                if(i != gameManager.currentIndexOfCar)
                    cars[i].visibility = View.INVISIBLE
                else
                    cars[i].visibility = View.VISIBLE
            }
        }

    private fun changeActivity() {
        sound.stop()
        moveDetector.stopListening()
        Log.d("Game Status", "Game Over!")
        val intent = saveSharePrefrences()
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