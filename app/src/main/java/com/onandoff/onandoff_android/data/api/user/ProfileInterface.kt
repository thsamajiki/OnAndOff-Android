package com.onandoff.onandoff_android.data.api.user

import com.onandoff.onandoff_android.data.model.ProfileListResponse
import com.onandoff.onandoff_android.data.model.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProfileInterface {

    @POST("/profiles")
    fun profileCreate(
        @Body profileName : MultipartBody.Part,
        @Part personaName:  MultipartBody.Part,
        @Part statusMessage:  MultipartBody.Part,
        @Part image: MultipartBody.Part?=null,
    ): Call<ProfileResponse>

    @GET("/profiles/my-profiles")
    fun profileCheck(): Call<ProfileListResponse>
}