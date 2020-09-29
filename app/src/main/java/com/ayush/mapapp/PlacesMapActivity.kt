package com.ayush.mapapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ayush.mapapp.Model.MyPlaces

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class PlacesMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val myplaces=intent.getSerializableExtra("Maps_Extra") as MyPlaces
        val places=myplaces.Places
        supportActionBar?.title=myplaces.Title
        val boundsBuilder=LatLngBounds.builder()
        for(place in places)
        {
            val langlat = LatLng(place.latitude, place.longitude)
            boundsBuilder.include(langlat)
            mMap.addMarker(MarkerOptions().position(langlat).title(place.Title).snippet(place.description))
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),100,100,0))
    }
}