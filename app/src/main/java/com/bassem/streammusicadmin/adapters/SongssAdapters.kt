package com.bassem.streammusicadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bassem.streammusicadmin.R
import com.bassem.streammusicadmin.entities.Singer
import com.bassem.streammusicadmin.entities.Song
import com.bumptech.glide.Glide

class SongssAdapters(
    var singersList: List<Song>,
    val context: Context
) : RecyclerView.Adapter<SongssAdapters.ViewHolder>() {

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val title = item.findViewById<TextView>(R.id.itemSingerName)
        val image = item.findViewById<ImageView>(R.id.itemSingerPhoto)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.singer_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val singer = singersList[position]
        holder.title.text = singer.name
        val imageView = holder.image
        Glide.with(context).load(singer.coverLink).into(imageView)
    }

    override fun getItemCount(): Int {
        return singersList.size
    }
    fun fileter(list: List<Song>) {
        singersList = list
        notifyDataSetChanged()
    }
}