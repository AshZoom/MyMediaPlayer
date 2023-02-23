package com.practicum.mymediaplayer

import android.R
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.practicum.mymediaplayer.R.layout.activity_search)

        //findViewById<Button>(R.id.button_arrow_back_searching).setOnClickListener { finish() }
        val backButton =
            findViewById<Button>(com.practicum.mymediaplayer.R.id.button_arrow_back_searching)
        val inputTextSearch =
            findViewById<EditText>(com.practicum.mymediaplayer.R.id.inputTextSearch)
        val clearButton =
            findViewById<AppCompatButton>(com.practicum.mymediaplayer.R.id.clearTextSearch)

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputTextSearch.setText("")
            it.hideKeyboard()
        }
        var searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)

            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        inputTextSearch.addTextChangedListener(searchTextWatcher)


    }


    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}






