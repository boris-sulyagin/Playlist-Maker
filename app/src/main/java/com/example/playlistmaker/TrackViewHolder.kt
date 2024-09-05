package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val itemTrackNameView: TextView = itemView.findViewById(R.id.itemTrackName)
    private val itemArtistNameView: TextView = itemView.findViewById(R.id.itemArtistName)
    private val itemTrackTimeView: TextView = itemView.findViewById(R.id.itemTrackTime)
    private val itemArtworkUrl100View: ImageView = itemView.findViewById(R.id.item_image)

    fun bind(model: Track) {
        itemTrackNameView.text = "${model.trackName}"
        itemArtistNameView.text = "${model.artistName}"
        itemTrackTimeView.text = "${model.trackTime}"

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(itemArtworkUrl100View)
    }
}