package com.sarai.meditrack.data.remote

import retrofit2.http.GET

interface QuoteApiService {

    @GET("api/phrase")
    suspend fun getRandomQuote(): QuoteResponse

}