package com.example.cardbinder.data.remote

import com.example.cardbinder.model.MTGCard
import com.example.cardbinder.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScryfallAPI {
    @GET("/cards/search?order=cmc&q=o:")
    suspend fun getAllCards(@Query("page") page: Int): SearchResult

    @GET("/cards/search?order=cmc&")
    suspend fun searchCardsByName(@Query("q") name: String): SearchResult

    @GET("/cards/random")
    suspend fun getRandomCard(): MTGCard

    @GET("/cards/{Id}")
    suspend fun getCardById(@Path("Id") id: String): MTGCard
}