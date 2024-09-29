package com.example.myapplication55

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication55.databinding.ActivityMainBinding
import com.example.myapplication55.fragments.HighScore_Fragment
import com.example.myapplication55.fragments.MapFragment
import com.example.myapplication55.interfaces.Callback_HighScore_Item_Clicked
import com.example.myapplication55.models.Player
import com.example.myapplication55.utilities.DataManager
import com.example.myapplication55.utilities.LocationManager
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections

class HighScoreAndMapActivity : AppCompatActivity() {

    private lateinit var fragment_score : FrameLayout
    private lateinit var fragment_map : FrameLayout
    private lateinit var high_score_fragment : HighScore_Fragment
    private lateinit var high_map_fragment : MapFragment
    private lateinit var return_to_menu : MaterialButton
    private lateinit var binding: ActivityMainBinding
    private lateinit var location : LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score_and_map)
        location = LocationManager(this)
        location.askLocationPermissions(this)
        location.findLocation()

        findViews()
        setFragment()
    }

    private fun setFragment() {
        high_score_fragment = HighScore_Fragment()
        high_score_fragment.callbackHighScoreItemClicked=( object : Callback_HighScore_Item_Clicked{
            override fun highScoreItemClicked(lat: Double, lng: Double) {
                high_map_fragment.moveToLocation(lat,lng)
                }
            })

        supportFragmentManager.beginTransaction().add(R.id.fragment_score, high_score_fragment).commit()
        high_map_fragment = MapFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_map, high_map_fragment).commit()
        supportFragmentManager.executePendingTransactions()
        val playerList : List<Player> = getPlayerListData()
        if (playerList != null && !playerList.isEmpty()) {
            high_score_fragment.setPlayerList(playerList)
        }
    }

    fun getPlayerListData() :List<Player> {
        val json = Gson().toJson(DataManager(this).getListOfPlayers())
        if (json.equals("null")) {
            return Collections.emptyList()
        }
        return Gson().fromJson(json, object : TypeToken<List<Player>>() {}.type);
    }

    fun findViews() {
        fragment_score = findViewById(R.id.fragment_score)
        fragment_map = findViewById(R.id.fragment_map)
        return_to_menu = findViewById(R.id.return_to_Main)
        return_to_menu.setOnClickListener {
            changeToMenuActivity()
        }
        return_to_menu.visibility = View.VISIBLE


    }

    private fun changeToMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java);
        var bundle = Bundle()
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}