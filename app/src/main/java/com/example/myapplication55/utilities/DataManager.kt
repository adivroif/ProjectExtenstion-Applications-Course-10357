package com.example.myapplication55.utilities

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.myapplication55.MainActivity
import com.example.myapplication55.models.ListOfPlayers
import com.example.myapplication55.models.Player
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataManager {
    private var listOfPlayers: ArrayList<Player>
    private var list : ListOfPlayers = ListOfPlayers()
    private var instance: DataManager? = null
    private var prefs: SharedPreferences

    constructor(context: Context) {
        listOfPlayers = ListOfPlayers().getListOfPlayers()
        prefs = context.getSharedPreferences("MyPreference", MODE_PRIVATE)
        if(listOfPlayers.isNullOrEmpty())
            listOfPlayers = ArrayList<Player>()
    }

    fun getInstace():DataManager?
    {
        return instance
    }

    fun getListOfPlayers() : ArrayList<Player>
    {
        return this.listOfPlayers;
    }

    fun readSharedPreferences(context: Context): ArrayList<Player> {
        val players:String = readString(context,"list", " ").toString()
        var gson = Gson()
        var type = object : TypeToken<ArrayList<String>>(){}.type
        if(players.isNullOrEmpty()) {
            return gson.fromJson(players, type)
        } else return ArrayList<Player>()
    }

    fun addPlayer(player: Player) {
        list.addToList(player)
        sortPlayers()
    }

    private fun sortPlayers() {

        listOfPlayers.sortWith(compareBy { it.score })
    }


    fun saveString(context: Context, key: String, value: String) {
        var sharedPref = context.getSharedPreferences("MySharedPrefrences",Context.MODE_PRIVATE)
        with(sharedPref.edit())
        {
            putString(key,value)
            apply()
        }
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun readString(context: Context,key: String, def: String) : String? {
        val sharedPref = context.getSharedPreferences("MySharedPrefrences",Context.MODE_PRIVATE)
        return sharedPref.getString(key," ")
    }
}


