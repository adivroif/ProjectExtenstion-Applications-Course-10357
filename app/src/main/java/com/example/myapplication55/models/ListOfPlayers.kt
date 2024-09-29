package com.example.myapplication55.models

class ListOfPlayers {

    constructor()
    {
        if(listOfPlayers.isNullOrEmpty())
            listOfPlayers = ArrayList<Player>()
    }

    fun getListOfPlayers() : ArrayList<Player>
    {
        return listOfPlayers
    }

    fun addToList(player: Player)
    {
        listOfPlayers.add(player)
    }
    companion object
    {
        var listOfPlayers: ArrayList<Player> = ArrayList<Player>()
    }

}