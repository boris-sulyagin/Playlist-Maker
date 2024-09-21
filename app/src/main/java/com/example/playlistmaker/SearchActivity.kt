package com.example.playlistmaker

import android.annotation.SuppressLint

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapters.TrackAdapter
import com.example.playlistmaker.model.ItunesApiConst
import com.example.playlistmaker.model.ItunesSongSearch
import com.example.playlistmaker.model.TrackReturn
import com.google.android.material.appbar.MaterialToolbar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    companion object{
       private const val STRING_VALUE = "STRING_VALUE"
    }

    lateinit var inputEditText: EditText
    private lateinit var searchToolbar: MaterialToolbar
    lateinit var clearButton: ImageView
    private lateinit var recyclerTracksList: RecyclerView
    private lateinit var buttonUpdate: Button
    private lateinit var foundNothingPlaceholder: TextView
    private lateinit var communicationProblemsPlaceholder: LinearLayout

    private lateinit var recyclerHistory: RecyclerView
    private lateinit var historyList: LinearLayout
    private lateinit var buttonClearHistory: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var prefs: SharedPreferences

    private var searchInput: String = ""


    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesApiConst.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearch = retrofit.create(ItunesSongSearch::class.java)

    private val searchAdapter = TrackAdapter {
        searchHistory.add(it)
    }

    private val historyAdapter = TrackAdapter {
        searchHistory.add(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initToolbar()

        initSearch()

        inputText()

        initSearchResults()

        initHistory()

    }

    private fun initToolbar() {
        searchToolbar = findViewById(R.id.searchToolbar)
        searchToolbar.setOnClickListener{
            finish()
        }
    }

    private fun initSearch() {
        buttonUpdate = findViewById(R.id.buttonUpdate)
        clearButton = findViewById(R.id.search_close)
        inputEditText = findViewById(R.id.inputEditText)
        inputEditText.setText(searchInput)
        inputEditText.requestFocus()

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTrack()
            }
            false
        }
        buttonUpdate.setOnClickListener{
            getTrack()
        }

        clearButton.setOnClickListener {
            clearSearch()
        }
    }


    private fun initHistory() {
        buttonClearHistory = findViewById(R.id.button_clear_history)
        historyList = findViewById(R.id.history_list)
        recyclerHistory = findViewById(R.id.recyclerViewHistory)

        recyclerHistory.adapter = historyAdapter
        prefs = getSharedPreferences(Constants.HISTORY_TRACKS_SHARED_PREF, MODE_PRIVATE)
        searchHistory = SearchHistory(prefs)

        if (inputEditText.text.isEmpty()) {
            historyAdapter.tracks = searchHistory.get()
            if (historyAdapter.tracks.isNotEmpty()) {
                showContent(Content.TRACKS_HISTORY)
            }
        }

        buttonClearHistory.setOnClickListener {
            clearTracksHistory()
        }
    }

    private fun clearTracksHistory() {
        searchHistory.clear()
        showContent(Content.SEARCH_RESULT)
    }

    private fun initSearchResults() {
        recyclerTracksList = findViewById(R.id.recyclerTracksList)
        recyclerTracksList.adapter = searchAdapter
        recyclerTracksList.layoutManager = LinearLayoutManager(this)
    }

    private fun clearSearch() {
        inputEditText.setText("")
        historyAdapter.tracks = searchHistory.get()
        if (historyAdapter.tracks.isNotEmpty()) {
            showContent(Content.TRACKS_HISTORY)
        } else {
            showContent(Content.SEARCH_RESULT)
        }
        val view = this.currentFocus
        if (view != null) {
            val input = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun buttonSearchClearVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    private fun inputText() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = buttonSearchClearVisibility(s)
                searchInput = inputEditText.text.toString()

                if (inputEditText.hasFocus() && searchInput.isNotEmpty()) {
                    showContent(Content.SEARCH_RESULT)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                showContent(Content.SEARCH_RESULT)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STRING_VALUE, searchInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)
        searchInput = savedInstanceState.getString(STRING_VALUE, searchInput)
        inputEditText.setText(searchInput)
    }

    private fun getTrack() {
        if (searchInput.isNotEmpty()) {
            serviceSearch.search(searchInput).enqueue(object : Callback<TrackReturn> {
                override fun onResponse(
                    call: Call<TrackReturn>,
                    response: Response<TrackReturn>
                ) {
                    when (response.code()) {
                        ItunesApiConst.SUCCESS_CODE -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                searchAdapter.tracks = response.body()?.results!!
                                showContent(Content.SEARCH_RESULT)
                            } else {
                                showContent(Content.NOT_FOUND)
                            }
                        }
                        else -> {
                            showContent(Content.ERROR)
                        }
                    }
                }

                override fun onFailure(call: Call<TrackReturn>, t: Throwable) {
                    showContent(Content.ERROR)
                }
            })
        }
    }

    private fun showContent(content: Content){
        foundNothingPlaceholder = findViewById(R.id.foundNothingPlaceholder)
        communicationProblemsPlaceholder = findViewById(R.id.communicationProblemsPlaceholder)

        when(content) {
            Content.NOT_FOUND -> {
                recyclerTracksList.visibility = View.GONE
                historyList.visibility = View.GONE
                foundNothingPlaceholder.visibility = View.VISIBLE
                communicationProblemsPlaceholder.visibility = View.GONE
            }
            Content.ERROR -> {
                recyclerTracksList.visibility = View.GONE
                historyList.visibility = View.GONE
                foundNothingPlaceholder.visibility = View.GONE
                communicationProblemsPlaceholder.visibility = View.VISIBLE
            }
            Content.SEARCH_RESULT -> {
                recyclerTracksList.visibility = View.VISIBLE
                historyList.visibility = View.GONE
                foundNothingPlaceholder.visibility = View.GONE
                communicationProblemsPlaceholder.visibility = View.GONE
            }
            Content.TRACKS_HISTORY -> {
                recyclerTracksList.visibility = View.GONE
                historyList.visibility = View.VISIBLE
                foundNothingPlaceholder.visibility = View.GONE
                communicationProblemsPlaceholder.visibility = View.GONE
            }
        }
    }
}



