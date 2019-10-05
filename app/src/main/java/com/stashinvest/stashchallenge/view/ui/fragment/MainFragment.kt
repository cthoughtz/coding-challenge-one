package com.stashinvest.stashchallenge.view.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.stashinvest.stashchallenge.App
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.model.ImageResult
import com.stashinvest.stashchallenge.model.api.StashImageService
import com.stashinvest.stashchallenge.view.ui.adapter.ViewModelAdapter
import com.stashinvest.stashchallenge.view.ui.factory.ImageFactory
import com.stashinvest.stashchallenge.util.SpaceItemDecoration
import com.stashinvest.stashchallenge.viewmodel.ImageViewModel
import com.stashinvest.stashchallenge.viewmodel.StashViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var adapter: ViewModelAdapter
    @Inject
    lateinit var stashImageService: StashImageService
    @Inject
    lateinit var imageFactory: ImageFactory

    private val space: Int by lazy { requireContext().resources.getDimensionPixelSize(R.dimen.image_space) }

    lateinit var viewmodel: StashViewModel

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.appComponent.inject(this)
        viewmodel = ViewModelProviders.of(this, object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return StashViewModel(stashImageService) as T
            }
        }).get(StashViewModel::class.java)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewmodel.searchImagesLiveData.observe(this, Observer { imageResponse ->
            adapter.setViewModels(imageResponse.images.map {
                ImageViewModel(it) { id, url ->
                    val dialog = PopUpDialogFragment()
                    val arguments = Bundle().apply {
                        putString("id", id)
                        putString("url", url)
                    }
                    dialog.arguments = arguments
                    dialog.show(childFragmentManager, "")
                }
            })
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        searchPhrase.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                return@setOnEditorActionListener true
            }
            false
        }

        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpaceItemDecoration(space, space, space, space))
    }

//    override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
//        progressBar.visibility = GONE
//
//        if (response.isSuccessful) {
//            val images = response.body()?.images ?: listOf()
//            updateImages(images)
//        } else {
//            Toast.makeText(activity, response.message().toString(),Toast.LENGTH_LONG).show()
//        }
//    }

//    override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
//        progressBar.visibility = GONE
//        //todo - show error
//    }

    private fun search() {
        progressBar.visibility = View.VISIBLE
        viewmodel.fetchSearchImages(searchPhrase.text.toString())
    }

    private fun updateImages(images: List<ImageResult>) {
        val viewModels = images.map { imageFactory.createImageViewModel(it, ::onImageLongPress) }
        adapter.setViewModels(viewModels)

        val viewModel = images.map { imageFactory.createImageViewModel(it, ::onImageLongPress) }
        adapter.setViewModels(viewModel)
    }

    fun onImageLongPress(id: String, uri: String?) {
        Log.d("APP_DEBUG", id)
    }
}
