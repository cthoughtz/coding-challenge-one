package com.stashinvest.stashchallenge.viewmodel

import android.view.View
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.model.ImageResult
import com.stashinvest.stashchallenge.view.ui.viewholder.ImageViewHolder
import com.stashinvest.stashchallenge.viewmodel.ViewModelType.STASH_IMAGE

class ImageViewModel(private val imageResult: ImageResult,
                     private val listener: (id: String, uri: String?) -> Unit)
    : BaseViewModel<ImageViewHolder>(R.layout.cell_image_layout) {
    
    override fun createItemViewHolder(view: View): ImageViewHolder {
        return ImageViewHolder(view)
    }
    
    override fun bindItemViewHolder(holder: ImageViewHolder) {
        holder.bind(imageResult) { listener(imageResult.id, imageResult.thumbUri) }
    }
    
    override val viewType: ViewModelType
        get() = STASH_IMAGE
}
