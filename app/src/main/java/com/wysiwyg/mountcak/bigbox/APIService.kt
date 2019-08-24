package com.wysiwyg.mountcak.bigbox

import com.wysiwyg.mountcak.data.model.SMSResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface APIService {

    @POST("/sms-notification/1.0.0/messages")
    @FormUrlEncoded
    fun postSMS(
        @Header("x-api-key") apiKey: String,
        @Field("msisdn") phone: String,
        @Field("content") content: String
    ): Call<SMSResponse>
}