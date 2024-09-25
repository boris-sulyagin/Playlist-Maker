package com.example.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.TracksDiffCallback
import com.example.playlistmaker.viewHolders.TrackViewHolder

class TrackAdapter(
    private val clickListener: TrackClickListener
): RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()
        set(newTrackList) {
            val diffResult = DiffUtil.calculateDiff(
                TracksDiffCallback(field, newTrackList)
            )
            field = newTrackList
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[position]) }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

}