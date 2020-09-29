package com.ayush.mapapp

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ayush.mapapp.Model.MyPlaces
import com.ayush.mapapp.Model.Places

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class CreateMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var markers = arrayListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        supportActionBar?.title = intent.getStringExtra("title")

        mapFragment.view.let {
            if (it != null) {
                Snackbar.make(it,"Long press anywhere to add a place",Snackbar.LENGTH_INDEFINITE)
                    .setAction("ok",{})
                    .setActionTextColor(ContextCompat.getColor(this,android.R.color.white)).show()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_map,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.miSave)
        {
            if(markers.isEmpty())
            {
                Toast.makeText(this,"long press anywhere to select",Toast.LENGTH_SHORT).show()
                return true
            }
            val places=markers.map { marker ->Places(marker.title,marker.snippet,marker.position.longitude,marker.position.latitude)}
            val myPlaces=MyPlaces(intent.getStringExtra("title"),places)
            val data=Intent(this,PlacesMapActivity::class.java)
            data.putExtra("Map",myPlaces)
            setResult(Activity.RESULT_OK,data)
            finish()
            return true

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnInfoWindowClickListener { markerToDelete ->
            markers.remove(markerToDelete)
            print("deleted")
            markerToDelete.remove()
        }
        mMap.setOnMapLongClickListener {
            showDialogBox(it)
        }

    }

    private fun showDialogBox(latlang: LatLng) {
        val viewEnter=LayoutInflater.from(this).inflate(R.layout.dialog_enter_place,null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("decide the marker")
            .setView(viewEnter)
            .setPositiveButton("OK", null)
            .setNegativeButton("cancel", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title=viewEnter.findViewById<EditText>(R.id.etTitle).text.toString()
            val description=viewEnter.findViewById<EditText>(R.id.etDescription).text.toString()

            if(title.trim().isEmpty()||description.trim().isEmpty())
            {
                Toast.makeText(this,"Title and description should not be empty",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val marker = mMap.addMarker(
                MarkerOptions().position(latlang).title(title)
                    .snippet(description)
            )
            markers.add(marker)
            dialog.dismiss()

        }

    }
}
