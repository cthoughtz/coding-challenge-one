package com.stashinvest.stashchallenge.model.api

import com.stashinvest.stashchallenge.model.ImageResponse
import com.stashinvest.stashchallenge.model.MetadataResponse
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StashImagesApi {
    @GET("search/images")
    fun searchImages(@Query("phrase") phrase: String,
                     @Query("fields") fields: String,
                     @Query("sort_order") sortOrder: String): Single<ImageResponse>

    @GET("images/{id}")
    fun getImageMetadata(@Path("id") id: String): Single<MetadataResponse>

    @GET("images/{id}/similar")
    fun getSimilarImages(@Path("id") id: String): Single<ImageResponse>
}
