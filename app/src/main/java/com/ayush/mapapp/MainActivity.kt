package com.ayush.mapapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayush.mapapp.Adapter.MapsAdapter
import com.ayush.mapapp.Model.MyPlaces
import com.ayush.mapapp.Model.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.net.FileNameMap

class MainActivity : AppCompatActivity() {
    private val FILENAME="UserMaps.data"
    var PlacesList = arrayListOf<MyPlaces>()
    lateinit var mapsadapter:MapsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //set LayoutManager for recycler view
        rvMaps.layoutManager = LinearLayoutManager(this)
        //set adapter for the recycler view
        PlacesList.addAll(deserializeMyPlaces(this))
        mapsadapter = MapsAdapter(this, PlacesList)
        rvMaps.adapter= mapsadapter
        btnCreateMap.setOnClickListener {
            showDialogBox()
        }
    }
    private  fun getDataFile(context: Context):File
    {
        return File(context.filesDir,FILENAME)
    }
    private fun serializeMyPlaces(context: Context,myPlaces:List<MyPlaces>)
    {
        ObjectOutputStream(FileOutputStream(getDataFile(context))).use { it.writeObject(myPlaces) }
    }
    private fun deserializeMyPlaces(context: Context):List<MyPlaces>
    {
        val dataFile=getDataFile(context)
        if(!dataFile.exists())
        {
            return emptyList()
        }
        ObjectInputStream(FileInputStream(dataFile)).use { return it.readObject() as List<MyPlaces> }
    }
    private fun showDialogBox() {
        val viewCreate= LayoutInflater.from(this).inflate(R.layout.dialog_create_map,null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Create a new map")
            .setView(viewCreate)
            .setPositiveButton("OK", null)
            .setNegativeButton("Cancel", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
             val titleMap=viewCreate.findViewById<EditText>(R.id.etTitleMap).text.toString()

            if(titleMap.trim().isEmpty())
            {
                Toast.makeText(this,"Title should not be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val i= Intent(this,CreateMapActivity::class.java)
            i.putExtra("title",titleMap)
            startActivityForResult(i,20)
            dialog.dismiss()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==20 && resultCode== RESULT_OK){
            val maps=data?.getSerializableExtra("Map")as MyPlaces
            PlacesList.add(maps)
            mapsadapter.notifyItemInserted(PlacesList.size-1)
            serializeMyPlaces(this,PlacesList)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
