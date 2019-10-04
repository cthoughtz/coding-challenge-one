package com.stashinvest.stashchallenge.view.ui.factory

import com.stashinvest.stashchallenge.model.ImageResult
import com.stashinvest.stashchallenge.viewmodel.ImageViewModel

import javax.inject.Inject

class ImageFactory @Inject constructor() {
    fun createImageViewModel(imageResult: ImageResult,
                             listener: (id: String, uri: String?) -> Unit): ImageViewModel {
        return ImageViewModel(imageResult, listener)
    }
}
