package com.stashinvest.stashchallenge.view.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.stashinvest.stashchallenge.App
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.model.api.StashImageService
import com.stashinvest.stashchallenge.viewmodel.StashViewModel
import kotlinx.android.synthetic.main.fragment_dialog_popup.*
import javax.inject.Inject

class PopUpDialogFragment : DialogFragment() {


    @Inject
    lateinit var stashImageService: StashImageService

    lateinit var viewmodel: StashViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)
        viewmodel = ViewModelProviders.of(this, object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return StashViewModel(stashImageService) as T
            }
        }).get(StashViewModel::class.java)
        observeViewModel()
        val id = arguments?.getString("id")
        val url = arguments?.getString("url")
        Picasso.get()
                .load(url)
                .into(imageView)
        viewmodel.fetchSimilarImages(id)
        viewmodel.fetchImagesMetadata(id)
    }


    private fun observeViewModel() {
        viewmodel.imageMetaDataLiveData.observe(this, Observer { metadata ->
            metadata.metadata.firstOrNull()?.let {
                Log.e("title", it.title)
                Log.e("artist", it.artist)
                titleView.text = it.title

            }
        })
        viewmodel.similarImagesLiveData.observe(this, Observer { imageResponse ->
            val inagesViews = mutableListOf<ImageView>(similarImageView1, similarImageView2, similarImageView3)

            imageResponse.images.forEachIndexed { index, imgView ->
                if(index< 3) {
                    Picasso.get()
                            .load(imageResponse.images[index].thumbUri)
                            .into(inagesViews[index])
                }
            }

        })
    }
}
