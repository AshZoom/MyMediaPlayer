package com.practicum.mymediaplayer

import com.practicum.mymediaplayer.domain.models.Track

//класс  ответа от сервера
class ITunesResponse(val resultCount: Int, val results: List<Track>)