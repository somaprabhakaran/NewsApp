package com.prabhakaran.newsapp.rest

import com.prabhakaran.newsapp.model.NewsResponse
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @GET("news")
    fun getCategoryAllNews(@Query("category") category: String): Flowable<NewsResponse>


}