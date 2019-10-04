package com.stashinvest.stashchallenge.model.api

import com.stashinvest.stashchallenge.model.ImageResponse
import com.stashinvest.stashchallenge.model.MetadataResponse
import io.reactivex.Single
import javax.inject.Inject

class StashImageService @Inject constructor() {
    
    @Inject
    lateinit var api: StashImagesApi

    fun searchImages(phrase: String): Single<ImageResponse> {
        return api.searchImages(phrase, FIELDS, SORT_ORDER)
    }

    fun getImagesMetadata(id: String): Single<MetadataResponse> {
        return api.getImageMetadata(id)
    }

    fun getSimilarImages(id: String): Single<ImageResponse> {
        return api.getSimilarImages(id)
    }

    companion object {
        val FIELDS = "id,title,thumb"
        val SORT_ORDER = "best"
    }
}
