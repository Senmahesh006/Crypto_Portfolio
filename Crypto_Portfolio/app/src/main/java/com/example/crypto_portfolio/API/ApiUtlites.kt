package com.example.crypto_portfolio.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtlites {

    fun getInstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(" https://api.coinmarketcap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}