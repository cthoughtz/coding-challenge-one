package com.stashinvest.stashchallenge.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stashinvest.stashchallenge.model.ImageResponse
import com.stashinvest.stashchallenge.model.api.StashImageService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StashViewModel(val apiService : StashImageService) : ViewModel() {

    private val compositeDisposable= CompositeDisposable()
    var stashLiveData= MutableLiveData<ImageResponse>()

    fun fetchData(phrase: String?) {

        compositeDisposable.add(
                apiService.searchImages(phrase?:"")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess{
                            stashLiveData.postValue(it)
                        }
                        .subscribe()
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}