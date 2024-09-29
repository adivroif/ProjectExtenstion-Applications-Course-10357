package com.example.myapplication55.interfaces

import com.example.myapplication55.models.Player

interface CallbackOfPlayer {
    fun playerClicked(player: Player, position: Int)

}