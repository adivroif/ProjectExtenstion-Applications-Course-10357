package com.example.myapplication55.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class LocationManager (private var context: Context){
    private lateinit var location : Location
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener
    private var lng:Double = 0.0
    private var lat:Double = 0.0
    val DEFAULT_LAT : Double = 31.771959
    val DEFAULT_LON : Double = 35.217018

    fun findLocation() {
        do {
            Listen()
        } while (lng == 0.0 && lat == 0.0)
    }

    fun getLng(): Double {
        return lng
    }

    fun getLat(): Double {
        return lat
    }

    fun askLocationPermissions(activity: Activity?) {
        if (ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(
                    this.context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    1
                )
            }
            if (ContextCompat.checkSelfPermission(
                    this.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        }
    }

    private fun Listen() {
        locationManager =
            this.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            lat = DEFAULT_LAT
            lng = DEFAULT_LON
            return
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                lat = DEFAULT_LAT;
                lng = DEFAULT_LON;
                Log.d("GPS_STATUS", "GPS IS OFF ON USER'S PHONE!!!");
                return
            }
            location =
                locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER)!!
            if (location == null) {
                requestLocationUpdat()
            }
            else{
                lng = location.getLongitude()
                lat = location.getLatitude()
            }
        }
    }
    fun requestLocationUpdat() {
        locationListener = LocationListener {
            fun onLocationChanged(location: Location) {
                handleLocation(location)
                locationManager.removeUpdates(locationListener)
            }
        }
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager.requestLocationUpdates(
            android.location.LocationManager.GPS_PROVIDER,
            0, // Minimum time interval between updates in milliseconds
            0f, // Minimum distance between updates in meters
            locationListener
        );
    }
    fun  handleLocation(location: Location) {
        if (location != null) {
            lng = location.getLongitude();
            lat = location.getLatitude();
        } else {
            lng = DEFAULT_LON;
            lat = DEFAULT_LAT;
        }

    }
}
