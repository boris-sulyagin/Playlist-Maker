package com.example.playlistmaker
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {

    private var string: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val searchToolbar = findViewById<MaterialToolbar>(R.id.searchToolbar)

        searchToolbar.setOnClickListener{
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.search_close)

        clearButton.setOnClickListener{
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                string = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        // put string value
        super.onSaveInstanceState(outState)
        outState.putString("string_value", string)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)
        string = savedInstanceState.getString("string_value", string)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.setText(string)
    }
}
private fun clearButtonVisibility(s: CharSequence?): Int {
    return if (s.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}