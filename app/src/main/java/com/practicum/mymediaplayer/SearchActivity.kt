package com.practicum.mymediaplayer

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val trackSaved = ArrayList<Track>()
const val TRACKS_SAVED = "tracks_saved"
const val TRACK = "track"


class SearchActivity : AppCompatActivity() {
    private lateinit var inputTextSearch: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var trackNotFound: ImageView
    private lateinit var trackList: RecyclerView
    private lateinit var updateConnection: Button
    private lateinit var youLookingFor: LinearLayout
    private lateinit var searchTrackList: RecyclerView


    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter()
    private val adapterSavedTracks = TrackSavedAdapter()

    //в методе showmessage(): если connectionError=true-проблемы со связью
    //если connectionError=false-трек не найден
    private var connectionError = false


    //работа с Edit Text:
    // TEXT_EDITTEXT -ключ, по которому  будем сохранять и восстанавливать   текст
    //inputTextSearch.text.toString() - текст который нужно сохранить

    //сохранение состояния EditText в жизненном цикле SearchActivity после onStop
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_EDITTEXT, inputTextSearch.text.toString())
    }

    //получаем сохранённый текст из EditText в жизненном цикле SearchActivity после onStart
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputTextSearch.setText(savedInstanceState.getString(TEXT_EDITTEXT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<Button>(R.id.button_arrow_back_searching)
        val clearButton = findViewById<AppCompatButton>(R.id.clearTextSearch)
        val clearHistory = findViewById<Button>(R.id.button_clear_history)
        youLookingFor = findViewById(R.id.history_layout)
        updateConnection = findViewById(R.id.button_update)
        inputTextSearch = findViewById(R.id.inputTextSearch)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        trackNotFound = findViewById(R.id.track_not_found)
        trackList = findViewById(R.id.track_recyclerView)
        searchTrackList = findViewById(R.id.search_track_recyclerView)


        //адаптер для треков полученных от сервера
        adapter.tracks = tracks
        trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackList.adapter = adapter

        //адаптер для сохраненных треков
        adapterSavedTracks.trackSaved = trackSaved
        searchTrackList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchTrackList.adapter = adapterSavedTracks
        adapterSavedTracks.notifyDataSetChanged()

        //считываем треки, сохраненные в SharPrefernces
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCE, MODE_PRIVATE)
        trackSaved.addAll(readFromSP(sharedPrefs))

        // выход из Activity_Search
        backButton.setOnClickListener {
            finish()
        }

        //код для Истории поиска:
        //если EditText в фокусе и строка поиска пустая 'История поиска' Visible, иначе GONE
        inputTextSearch.setOnFocusChangeListener { view, hasFocus ->

            if (hasFocus && inputTextSearch.text.isEmpty() && trackSaved.size != 0) {
                youLookingFor.visibility = View.VISIBLE
                trackList.visibility = View.GONE
            } else {
                youLookingFor.visibility = View.GONE
                trackList.visibility = View.VISIBLE
            }
            clearDisplay()
        }

        //если EditText в фокусе и пользователь вводит  текст  'История поиска'  GONE
        inputTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (inputTextSearch.hasFocus() && p0?.isEmpty() == true&& trackSaved.size != 0) {
                    youLookingFor.visibility = View.VISIBLE
                    trackList.visibility = View.GONE
                } else {
                    youLookingFor.visibility = View.GONE
                    trackList.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // onClick слушатель для кнопки сброса текста
        clearButton.setOnClickListener {
            inputTextSearch.setText("")
            it.hideKeyboard()
        }


        //видимость clearButton в строке EditText
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = changeButtonVisibility(s)
                clearDisplay()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inputTextSearch.addTextChangedListener(searchTextWatcher)


        // обработка нажатия на кнопку Done в меню клавиатуры
        inputTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searching()
                true
            }
            false
        }

        updateConnection.setOnClickListener {
            searching()
        }
        //очищаем список сохраненных треков по нажатию Clear History(Очистить историю)

        clearHistory.setOnClickListener {
            trackSaved.clear()
            val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCE, MODE_PRIVATE)
            writeToSP(sharedPrefs, trackSaved)
            youLookingFor.visibility = View.GONE
            adapterSavedTracks.notifyDataSetChanged()
        }
    }

    //сохраняем список выбранных треков в SharePreferences при выходе из SearchActivity
    override fun onStop() {
        super.onStop()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCE, MODE_PRIVATE)
        writeToSP(sharedPrefs, trackSaved)
        trackSaved.clear()
    }

    override fun onPause() {
        super.onPause()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCE, MODE_PRIVATE)
        writeToSP(sharedPrefs, trackSaved)
        //trackSaved.clear()

    }

    override fun onResume() {
        super.onResume()
        //адаптер для сохраненных треков
        adapterSavedTracks.trackSaved = trackSaved
        searchTrackList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchTrackList.adapter = adapterSavedTracks
        adapterSavedTracks.notifyDataSetChanged()

        //считываем треки, сохраненные в SharPrefernces
        val sharedPrefsPause = getSharedPreferences(PLAYLIST_MAKER_PREFERENCE, MODE_PRIVATE)
        trackSaved.addAll(readFromSP(sharedPrefsPause))
        //удаляем одинаковые треки из списка сохраненных треков
        val distinctList = trackSaved.distinct()
        trackSaved.clear()
        //формируем новый список сохраненных треков
        trackSaved.addAll(distinctList)
    }

    // поисковый HTTP-запрос к веб-серверу iTunes и получение ответа в фоновом потоке
    private fun searching() {

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
                            adapterSavedTracks.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            connectionError = false
                            showMessage(
                                getString(R.string.nothing_found),
                                "",
                                connectionError
                            )
                        } else {
                            showMessage()
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

    // Метод clearButtonVisibility устанавливает видимость кнопки сброса текста.
    private fun changeButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    // Метод , который скрывает клавиатуру
    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    //Метод отображения сообщений об ошибках:
    // 1.Трек не найден и
    // 2.Отсутствие связи  с сервером
    fun showMessage(
        text: String = "",
        additionalMessage: String = "",
        connection: Boolean = false
    ) {
        if (text.isNotEmpty()) {
            if (connection) {
                trackNotFound.setImageResource(R.drawable.il_internet_light_mode)
                updateConnection.visibility = View.VISIBLE
            } else {
                trackNotFound.setImageResource(R.drawable.il_search_light_mode)
            }
            youLookingFor.visibility = View.GONE
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
            clearDisplay()

        }
    }

    //пустой экран (без сообщений о проблемах со связью и не найденных треках)
    private fun clearDisplay() {
        placeholderMessage.visibility = View.GONE
        trackNotFound.visibility = View.GONE
        updateConnection.visibility = View.GONE
    }


    // чтение из SharedPreferences
    private fun readFromSP(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(TRACKS_SAVED, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    // запись в SharedPreferences
    private fun writeToSP(sharedPreferences: SharedPreferences, tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_SAVED, json)
            .apply()
    }

    companion object {
        private const val TEXT_EDITTEXT = "TEXT_EDITTEXT"
    }
}






