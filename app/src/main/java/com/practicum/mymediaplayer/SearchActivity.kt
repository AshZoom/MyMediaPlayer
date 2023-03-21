package com.practicum.mymediaplayer

import android.app.UiModeManager
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class SearchActivity : AppCompatActivity() {
    private lateinit var inputTextSearch: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var trackNotFound: ImageView
    private lateinit var trackList: RecyclerView
    private lateinit var updateConnection: Button


    private val iTunesBaseUrl = "https://itunes.apple.com"


    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private val tracks = ArrayList<Track>()

    private val adapter = TrackAdapter()

    //в методе showmessage(): если connectionError=true-проблемы со связью
    //если connectionError=false-трек не найден
    private var connectionError = false


    //TEXT_EDITTEXT -ключ, по которому  будем сохранять и восстанавливать   текст
    //inputTextSearch.text.toString() - текст который нужно сохранить

    companion object {
        private const val TEXT_EDITTEXT = "TEXT_EDITTEXT"
    }

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
        val clearButton = findViewById<AppCompatButton>(R.id.clearTextSearch)
        updateConnection = findViewById(R.id.button_update)
        inputTextSearch = findViewById(R.id.inputTextSearch)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        trackNotFound = findViewById(R.id.track_not_found)
        trackList = findViewById(R.id.track_recyclerView)

        adapter.tracks = tracks
        trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackList.adapter = adapter


        // выход из Activity_Search

        backButton.setOnClickListener {
            finish()
        }

        // код для  EditText

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


// обработка нажатия на кнопку Done в меню клавиатуры

        inputTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                searching()

            }

            true
        }
        false

        updateConnection.setOnClickListener {
            searching()
        }


    }


    // поисковый HTTP-запрос к веб-серверу iTunes и получение ответа в фоновом потоке
    fun searching() {

        iTunesService.search(inputTextSearch.text.toString()).enqueue(
            object : Callback<ITunesResponse> {

                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>

                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        updateConnection.visibility = View.GONE
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            connectionError = false
                            showMessage(
                                getString(R.string.nothing_found),
                                "",
                                connectionError
                            )
                        } else {
                            showMessage("", "", connectionError)
                        }
                    } else {
                        connectionError = true
                        showMessage(
                            getString(R.string.something_went_wrong),
                            response.code().toString(), connectionError
                        )
                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    connectionError = true
                    showMessage(
                        getString(R.string.something_went_wrong),
                        t.message.toString(), connectionError
                    )
                    updateConnection.visibility = View.VISIBLE
                }
            })

    }

    //класс  ответа от сервера
    class ITunesResponse(val resultCount: Int, val results: List<Track>)

    //интерфейс API запроса к серверу
    interface ITunesApi {
        @GET("/search?entity=song")
        fun search(@Query("term") text: String): Call<ITunesResponse>
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

    //Метод отображения сообщений об ошибках:
    // 1.Трек не найден и
    // 2.Отсутсвие связи  с сервером
    //uiModeManager используем для переключения изображения для темной и светлой темы
    fun showMessage(text: String, additionalMessage: String, connection: Boolean) {

        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

        if (text.isNotEmpty()) {
            if (connection) {
                when (uiModeManager.nightMode) {
                    UiModeManager.MODE_NIGHT_NO -> trackNotFound.setImageResource(R.drawable.il_internet_light_mode)
                    UiModeManager.MODE_NIGHT_YES -> trackNotFound.setImageResource(R.drawable.il_internet_dark_mode)
                }
            } else {
                trackNotFound.setImageResource(R.drawable.il_search_light_mode)
                when (uiModeManager.nightMode) {
                    UiModeManager.MODE_NIGHT_NO -> trackNotFound.setImageResource(R.drawable.il_search_light_mode)
                    UiModeManager.MODE_NIGHT_YES -> trackNotFound.setImageResource(R.drawable.il_search_dark_mode)
                }

            }
            placeholderMessage.visibility = View.VISIBLE
            trackNotFound.visibility = View.VISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
            trackNotFound.visibility = View.GONE
        }
    }


}






