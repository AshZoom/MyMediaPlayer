package com.practicum.mymediaplayer

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SearchActivity : AppCompatActivity() {
    private lateinit var inputTextSearch: EditText

    companion object {
        private const val TEXT_EDITTEXT = "TEXT_EDITTEXT"
    }

    //TEXT_EDITTEXT -ключ, по которому  будем сохранять и восстанавливать   текст
    //inputTextSearch.text.toString() - текст который нужно сохранить
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_EDITTEXT, inputTextSearch.text.toString())
    }

    //получаем сохранённый текст
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputTextSearch.setText(savedInstanceState.getString(TEXT_EDITTEXT))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val backButton = findViewById<Button>(R.id.button_arrow_back_searching)
        inputTextSearch = findViewById(R.id.inputTextSearch)
        val clearButton = findViewById<AppCompatButton>(R.id.clearTextSearch)

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputTextSearch.setText("")
            it.hideKeyboard()
        }
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = changeButtonVisibility(s)

            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        inputTextSearch.addTextChangedListener(searchTextWatcher)


    }

    // Метод clearButtonVisibility устанавливает видимость кнопки сброса текста.
    private fun changeButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}






