package com.example.playlistmaker.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val itemTrackNameView: TextView = itemView.findViewById(R.id.itemTrackName)
    private val itemArtistNameView: TextView = itemView.findViewById(R.id.itemArtistName)
    private val itemTrackTimeView: TextView = itemView.findViewById(R.id.itemTrackTime)
    private val itemArtworkUrl100View: ImageView = itemView.findViewById(R.id.item_image)


    fun bind(model: Track) {
        itemTrackNameView.text = model.trackName
        itemArtistNameView.text = model.artistName
        itemTrackTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toInt())

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
            .into(itemArtworkUrl100View)
    }
}