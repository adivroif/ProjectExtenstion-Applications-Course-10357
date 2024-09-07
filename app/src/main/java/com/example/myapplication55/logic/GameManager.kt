package com.example.myapplication55.logic

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.myapplication55.utilities.Constants
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameManager(val lifeCount: Int = 3) {
    var currentIndexOfCar = 1

    var numberOfCrashs:Int = 0

    val isGameLost: Boolean
        get() = numberOfCrashs == lifeCount

    fun moveCarLeft() {
        if(currentIndexOfCar > 0)
            currentIndexOfCar-=1
    }

    fun moveCarRight() {
        if(currentIndexOfCar < 2)
            currentIndexOfCar+=1
    }
}