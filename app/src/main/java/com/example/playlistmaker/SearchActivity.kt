package com.example.playlistmaker



import android.content.Context
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
import com.example.playlistmaker.model.PlaceHolder
import com.example.playlistmaker.model.TrackReturn
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object{
       private const val STRING_VALUE = "STRING_VALUE"
    }

    private var searchInput: String = ""
    lateinit var inputEditText: EditText
    private lateinit var searchToolbar: MaterialToolbar
    lateinit var clearButton: ImageView
    private lateinit var recycler: RecyclerView
    private lateinit var buttonUpdate: Button
    private lateinit var foundNothingPlaceholder: TextView
    private lateinit var communicationProblemsPlaceholder: LinearLayout
    lateinit var tracksAdapter: TrackAdapter

    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesApiConst.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tracksSearch = retrofit.create(ItunesSongSearch::class.java)
    private val tracks = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerCreate(tracks)
        update()
        toolbarReturn()
        searchCreate()
        textInput()

    }

    private fun update() {
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener{
            findTrack()
        }
    }

    private fun toolbarReturn() {
        searchToolbar = findViewById(R.id.searchToolbar)
        searchToolbar.setOnClickListener{
            finish()
        }
    }

    private fun recyclerCreate(tracks: ArrayList<Track>) {
        recycler = findViewById(R.id.tracksList)
        recycler.layoutManager = LinearLayoutManager(this)
        tracksAdapter = TrackAdapter(tracks)
        recycler.adapter = tracksAdapter
    }

    private fun searchCreate() {
        clearButton = findViewById(R.id.search_close)
        inputEditText = findViewById(R.id.inputEditText)
        inputEditText.setText(searchInput)
        inputEditText.requestFocus()

        clearButton.setOnClickListener{
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputEditText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)

            foundNothingPlaceholder.isVisible = false
            communicationProblemsPlaceholder.isVisible = false
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTrack()
                true
            }
            false
        }
    }

    private fun textInput() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchInput = inputEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
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

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun findTrack() {
        tracksSearch.search(inputEditText.text.toString())
            .enqueue(object : Callback<TrackReturn> {
                override fun onResponse(call: Call<TrackReturn>, response: Response<TrackReturn>) {
                    if (searchInput.isNotEmpty() && !response.body()?.results.isNullOrEmpty() && response.code() == ItunesApiConst.SUCCESS_CODE) {
                        tracks.clear()
                        tracks.addAll((response.body()?.results!!))
                        tracksAdapter.notifyDataSetChanged()
                        showPlaceholder(PlaceHolder.SEARCH_RESULT)
                    } else {
                        showPlaceholder(PlaceHolder.NOT_FOUND)
                    }
                }

                override fun onFailure(call: Call<TrackReturn>, t: Throwable) {
                    showPlaceholder(PlaceHolder.ERROR)
                }
            })
    }

    private fun showPlaceholder(placeHolder: PlaceHolder){
        foundNothingPlaceholder = findViewById(R.id.foundNothingPlaceholder)
        communicationProblemsPlaceholder = findViewById(R.id.communicationProblemsPlaceholder)

        when(placeHolder) {
            PlaceHolder.NOT_FOUND -> {
                recycler.visibility = View.GONE
                foundNothingPlaceholder.visibility = View.VISIBLE
                communicationProblemsPlaceholder.visibility = View.GONE
            }
            PlaceHolder.ERROR -> {
                recycler.visibility = View.GONE
                foundNothingPlaceholder.visibility = View.GONE
                communicationProblemsPlaceholder.visibility = View.VISIBLE
            }
            else -> {
                recycler.visibility = View.VISIBLE
                foundNothingPlaceholder.visibility = View.GONE
                communicationProblemsPlaceholder.visibility = View.GONE
            }
        }
    }
}



