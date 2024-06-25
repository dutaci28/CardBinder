package com.example.cardbinder.data.remote

import com.example.cardbinder.model.MTGCard
import com.example.cardbinder.model.CardSearchResult
import com.example.cardbinder.model.RulingSearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScryfallAPI {
    @GET("/cards/search?order=cmc&q=o:")
    suspend fun getAllCards(@Query("page") page: Int): CardSearchResult

    @GET("/cards/search?order=cmc&")
    suspend fun searchCardsByName(@Query("q") name: String): CardSearchResult

    @GET("/cards/random")
    suspend fun getRandomCard(): MTGCard

    @GET("/cards/{Id}")
    suspend fun getCardById(@Path("Id") id: String): MTGCard

    @GET("/cards/search")
    suspend fun getCardPrintings(
        @Query("order") order: String = "released",
        @Query("q") q: String,
        @Query("unique") unique: String = "prints"
    ): CardSearchResult

    @GET("/cards/{Id}/rulings")
    suspend fun getCardRulings(@Path("Id") id: String): RulingSearchResult
}