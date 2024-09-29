package com.example.myapplication55.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import com.example.myapplication55.interfaces.Callback_MoveCallback
import com.example.myapplication55.interfaces.TiltCallBack

class MoveDetector (context: Context, private val moveCallback: TiltCallBack?) {
    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as android.hardware.SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
    private lateinit var sensorEventListener: SensorEventListener
    private var timestamp: Long = 0L
    private val ACCELERATION_THRESHOLD:Double = 2.0
    init {
        initEventListener()
    }

fun initEventListener(){
    sensorEventListener = object : SensorEventListener{
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            calculateMovements(x, y)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            //pass
        }

    }
}

    private fun calculateMovements(x: Float, y: Float) {
        var x1: String
        var speed: String
        if (x > 0)
            x1 = "Left"
        else
            x1 = "Right"
        if (y > 0)
            speed = "Slow"
        else
            speed = "Fast"
        if (System.currentTimeMillis() - timestamp > 500) {
            timestamp = System.currentTimeMillis()
            if (x > ACCELERATION_THRESHOLD || x < -ACCELERATION_THRESHOLD)
                if (moveCallback != null)
                    moveCallback.x(x1)
            if (y > ACCELERATION_THRESHOLD || y < -ACCELERATION_THRESHOLD) {
                if (moveCallback != null)
                    moveCallback.speed(speed)
            }
        }
    }

    fun startListening() {
        sensorManager.registerListener(sensorEventListener, sensor, SENSOR_DELAY_NORMAL)
    }

    fun stopListening() {
        sensorManager.unregisterListener(sensorEventListener,sensor)
    }
}


