package com.stashinvest.stashchallenge.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stashinvest.stashchallenge.model.ImageResponse
import com.stashinvest.stashchallenge.model.MetadataResponse
import com.stashinvest.stashchallenge.model.api.StashImageService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class StashViewModel(val apiService : StashImageService) : ViewModel() {

    private val compositeDisposable= CompositeDisposable()
    var searchImagesLiveData = MutableLiveData<ImageResponse>()
    var imageMetaDataLiveData = MutableLiveData<MetadataResponse>()
    var similarImagesLiveData = MutableLiveData<ImageResponse>()

    fun fetchSearchImages(phrase: String?) {

        compositeDisposable.add(
                apiService.searchImages(phrase?:"")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess{
                            searchImagesLiveData.postValue(it)
                        }
                        .subscribe()
        )
    }

    fun fetchImagesMetadata(id: String?){
        compositeDisposable.add(
                apiService.getImagesMetadata(id?:"")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess {
                            imageMetaDataLiveData.postValue(it)
                        }
                        .subscribe()
        )
    }

    fun fetchSimilarImages(id: String?) {
        compositeDisposable.add(
                apiService.getSimilarImages(id?:"")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess {
                            similarImagesLiveData.postValue(it)
                        }.subscribe()
        )
    }





    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}