package com.stashinvest.stashchallenge.view.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.stashinvest.stashchallenge.App
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.model.api.StashImageService
import com.stashinvest.stashchallenge.model.ImageResponse
import com.stashinvest.stashchallenge.model.ImageResult
import com.stashinvest.stashchallenge.view.ui.adapter.ViewModelAdapter
import com.stashinvest.stashchallenge.view.ui.factory.ImageFactory
import com.stashinvest.stashchallenge.util.SpaceItemDecoration
import com.stashinvest.stashchallenge.viewmodel.ImageViewModel
import com.stashinvest.stashchallenge.viewmodel.StashViewModel
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainFragment : Fragment(), Callback<ImageResponse> {
    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
    
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

         viewmodel =  ViewModelProviders.of(this, object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return StashViewModel(stashImageService) as T
            }
        }).get(StashViewModel::class.java)

        observeStashViewModel()
    }

    private fun observeStashViewModel() {
        viewmodel.stashLiveData.observe(this, Observer{ imageResponse ->
            adapter.setViewModels(imageResponse.images.map { ImageViewModel(it) { id, url->
                run {
                    Log.e("id", id)
                    Log.e("url", url)
                }
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
        viewmodel.fetchData(searchPhrase.text.toString())
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpaceItemDecoration(space, space, space, space))
    }
    
    override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
        progressBar.visibility = GONE
        
        if (response.isSuccessful) {
            val images = response.body()?.images ?: listOf()
            updateImages(images)
        } else {
            //todo - show error
        }
    }
    
    override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
        progressBar.visibility = GONE
        //todo - show error
    }
    
    private fun search() {
        progressBar.visibility = View.VISIBLE
        val call = stashImageService.searchImages(searchPhrase.text.toString())
       // call.enqueue(this)
       // call.enqueue

    }
    
    private fun updateImages(images: List<ImageResult>) {
        val viewModels = images.map { imageFactory.createImageViewModel(it, ::onImageLongPress) }
        adapter.setViewModels(viewModels)
    }
    
    fun onImageLongPress(id: String, uri: String?) {
        //todo - implement new feature
    }
}
