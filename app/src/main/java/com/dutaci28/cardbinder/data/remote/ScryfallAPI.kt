//CardBinder - a Magic: The Gathering collector's app.
//Copyright (C) 2024 Catalin Duta
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.(https://github.com/dutaci28/CardBinder?tab=GPL-3.0-1-ov-file#readme).
//If not, see <https://www.gnu.org/licenses/>.

package com.dutaci28.cardbinder.data.remote

import com.dutaci28.cardbinder.model.MTGCard
import com.dutaci28.cardbinder.model.CardSearchResult
import com.dutaci28.cardbinder.model.RulingSearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScryfallAPI {

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