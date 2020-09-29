package com.ayush.mapapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayush.mapapp.MainActivity
import com.ayush.mapapp.Model.MyPlaces
import com.ayush.mapapp.PlacesMapActivity

class MapsAdapter(val context:Context, val placesList: ArrayList<MyPlaces>) : RecyclerView.Adapter<MapsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapsAdapter.ViewHolder {
        val view=LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false)
        return(ViewHolder(view))
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userMap=placesList[position]
        holder.title.text=userMap.Title
        holder.title.setOnClickListener {
            val intent= Intent(context.applicationContext,PlacesMapActivity::class.java)
            intent.putExtra("Maps_Extra",userMap)
            context.startActivity(intent)
        }


    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title:TextView=itemView.findViewById(android.R.id.text1)

    }

}
