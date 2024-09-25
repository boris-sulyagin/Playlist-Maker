package com.example.playlistmaker

import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.model.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var trackName: TextView
    private lateinit var trackTime: TextView
    private lateinit var artistName: TextView
    private lateinit var albumIcon: ImageView
    private lateinit var collectionName: TextView
    private lateinit var collectionNameTitle: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        initToolbar()

        initTrackInfo()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.player_toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initTrackInfo() {
        val track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.trackTime)
        albumIcon = findViewById(R.id.track_icon)
        collectionName = findViewById(R.id.album_name)
        releaseDate = findViewById(R.id.release_date_data)
        primaryGenreName = findViewById(R.id.primary_genre_name)
        country = findViewById(R.id.country_data)

        Glide
            .with(albumIcon)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_512)
            .centerCrop()
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(
                        R.dimen.Indent_8dp
                    )
                )
            )
            .into(albumIcon)

        trackName.text = track.trackName
        artistName.text = track.artistName
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country

        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
        if (date != null) {
            val formatDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            releaseDate.text = formatDatesString
        }

        if (track.collectionName.isNotEmpty()) {
            collectionName.text = track.collectionName
        } else {
            collectionName.visibility = View.GONE
            collectionNameTitle.visibility = View.GONE
        }
    }
}