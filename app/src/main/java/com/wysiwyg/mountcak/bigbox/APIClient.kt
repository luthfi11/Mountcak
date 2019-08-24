package com.wysiwyg.mountcak.bigbox

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    companion object {
        fun getClient() : Retrofit {
            val BASE_URL = "https://api.thebigbox.id/"
            val retrofit: Retrofit = Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit
        }
    }
}