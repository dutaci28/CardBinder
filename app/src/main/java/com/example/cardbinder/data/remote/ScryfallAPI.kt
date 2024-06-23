package com.example.cardbinder.data.remote

import com.example.cardbinder.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ScryfallAPI {
    @GET("/cards/search?order=cmc&q=c%3Ared+pow%3D3")
    suspend fun getAllCards(@Query("page") page: Int): SearchResult
}