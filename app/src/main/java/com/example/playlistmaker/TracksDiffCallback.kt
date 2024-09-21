package com.example.playlistmaker

import androidx.recyclerview.widget.DiffUtil

class TracksDiffCallback(
    private val trackList: ArrayList<Track>,
    private val newTrackList: ArrayList<Track>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return trackList.size
    }

    override fun getNewListSize(): Int {
        return newTrackList.size
    }

    override fun areItemsTheSame(itemPosition: Int, newItemPosition: Int): Boolean {
        val track = trackList[itemPosition]
        val newTrack = newTrackList[newItemPosition]
        return track.trackId == newTrack.trackId
    }

    override fun areContentsTheSame(itemPosition: Int, newItemPosition: Int): Boolean {
        val track = trackList[itemPosition]
        val newTrack = newTrackList[newItemPosition]
        return track == newTrack
    }
}
