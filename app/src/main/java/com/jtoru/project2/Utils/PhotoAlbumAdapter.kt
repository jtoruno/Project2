package com.jtoru.project2.Utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jtoru.project2.R
import com.squareup.picasso.Picasso

class PhotoAlbumAdapter(context: Context) : RecyclerView.Adapter<PhotoAlbumAdapter.PhotoAlbumVH>(){
    private var mDataset: MutableList<String> = mutableListOf()
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotoAlbumVH {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.photos_row, p0, false)
        return PhotoAlbumAdapter.PhotoAlbumVH(v)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(p0: PhotoAlbumVH, p1: Int) {
        val uri = mDataset[p1]
        Picasso.get().load(uri).error(R.drawable.download).into(p0.image)
        p0.itemView.setOnClickListener {

        }
    }

    class PhotoAlbumVH(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var image: ImageView?= null

        init {
            image = v.findViewById(R.id.img_profileRow)
        }
    }

    fun setAlbum(album:MutableList<String>){
        mDataset = album
        notifyDataSetChanged()
    }
}