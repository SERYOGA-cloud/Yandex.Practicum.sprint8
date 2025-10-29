package com.msaggik.playlistmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {

    private var textSearch = ""

    private lateinit var inputSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputSearch = findViewById(R.id.input_search)
        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val buttonClear = findViewById<ImageView>(R.id.button_clear)

        savedInstanceState?.getString(KEY_TEXT_SEARCH)?.let { saved ->
            textSearch = saved
        }
        inputSearch.setText(textSearch)
        inputSearch.setSelection(textSearch.length)

        val listener = View.OnClickListener { v ->
            when (v?.id) {
                R.id.button_back -> {
                    onBackPressedDispatcher.onBackPressed()
                }
                R.id.button_clear -> {
                    inputSearch.setText("")
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                        ?.hideSoftInputFromWindow(inputSearch.windowToken, 0)
                }
            }
        }

        val inputSearchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {
                textSearch = s?.toString().orEmpty()
            }
        }

        inputSearch.addTextChangedListener(inputSearchWatcher)
        buttonBack.setOnClickListener(listener)
        buttonClear.setOnClickListener(listener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TEXT_SEARCH, inputSearch.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restored = savedInstanceState.getString(KEY_TEXT_SEARCH, TEXT_SEARCH_DEFAULT)
        textSearch = restored
        inputSearch.setText(restored)
        inputSearch.setSelection(restored.length)
    }

    companion object {
        const val KEY_TEXT_SEARCH = "KEY_SEARCH"
        const val TEXT_SEARCH_DEFAULT = ""
    }
}
