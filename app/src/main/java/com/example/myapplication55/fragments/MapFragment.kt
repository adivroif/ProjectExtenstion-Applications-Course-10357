package com.example.myapplication55.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.example.myapplication55.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {

    private var map: GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap ->
        // Add a marker near Sydney, Australia
        val location = LatLng(-34.0, 151.0)
        map = googleMap
        map?.addMarker(MarkerOptions().position(location).title("Player is here"))
        map?.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        return view
    }

    fun moveToLocation(lat: Double, lng: Double) {
        val latLng = LatLng(lat, lng)
        if (map != null) {
            map?.addMarker(MarkerOptions().position(latLng).title("Player position"))
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_map) as? SupportMapFragment
        mapFragment?.getMapAsync(callback)
    }

}



