package com.practicum.mymediaplayer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//интерфейс API запроса к серверу
interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunesResponse>
}