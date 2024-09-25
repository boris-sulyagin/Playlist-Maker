package com.example.playlistmaker.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesSongSearch {

    @GET("search?entity=song")
    fun search(
        @Query("term") text: String
    ) : Call<TrackReturn>
}