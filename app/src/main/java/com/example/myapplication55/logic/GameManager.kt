package com.example.myapplication55.logic

class GameManager(val lifeCount: Int = 3) {
    var currentIndexOfCar = 0
    var startIndexOfCar = 0
    var numberOfCrashs:Int = 0
    var score:Int = 0
    val isGameLost: Boolean
        get() = numberOfCrashs == lifeCount

    fun moveCarLeft() {
        if(currentIndexOfCar > 0)
            currentIndexOfCar-=1
    }

    fun moveCarRight() {
        if(currentIndexOfCar < 4)
            currentIndexOfCar+=1
    }

    fun addScore() {
        score+=10
    }
}