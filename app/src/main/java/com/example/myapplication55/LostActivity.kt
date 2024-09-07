package com.example.myapplication55

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication55.utilities.Constants
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class LostActivity : AppCompatActivity(){

    private lateinit var Lost_Text : MaterialTextView

    private lateinit var lost_return_to_new_game : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lost)
        findViwes()
        initViews()
    }

    private fun initViews() {
        lost_return_to_new_game.setOnClickListener{v-> returnToNewGame()}
        val bundle: Bundle? = intent.extras
        val message = bundle?.getString(Constants.STATUS_KEY)
        Lost_Text.text = buildString {
            append(message)
        }
    }
    private fun findViwes() {
        Lost_Text = findViewById(R.id.Lost_Text)
        lost_return_to_new_game = findViewById(R.id.lost_return_to_new_game)
    }

    private fun returnToNewGame() {
        Log.d("Game Status", "return to new game!")
        changeActivity()
    }

    private fun changeActivity() {
        val intent = Intent(this, MainActivity::class.java);
        var bundle = Bundle()
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


}