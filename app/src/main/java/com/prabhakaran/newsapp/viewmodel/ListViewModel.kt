package com.prabhakaran.newsapp.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prabhakaran.newsapp.R
import com.prabhakaran.newsapp.dao.NewsListDAO
import com.prabhakaran.newsapp.database.AppDatabase
import com.prabhakaran.newsapp.model.News
import com.prabhakaran.newsapp.model.NewsResponse
import com.prabhakaran.newsapp.rest.ApiClient
import com.prabhakaran.newsapp.rest.ApiService
import com.prabhakaran.newsapp.util.SharedHelper
import com.prabhakaran.newsapp.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ListViewModel(val activity: AppCompatActivity) : ViewModel()  {
    private var request: ApiService = ApiClient.buildService(ApiService::class.java)
    private val error = MutableLiveData<String>()
    private val isLoginSuccess = MutableLiveData<Boolean>()
    fun getError(): LiveData<String> = error
    var newsList: MutableLiveData<List<News>> =  MutableLiveData()
    private val disposable = CompositeDisposable()
    var isLoading: ObservableBoolean = ObservableBoolean(false)

    lateinit var newsListDao: NewsListDAO
    var appDatabase= AppDatabase.getDatabase(activity)

    init {
        getNews()
        newsListDao =  appDatabase.newsListDao()
    }

    fun getNews() {

        if (Utility.isNetworkAvailable(activity)) {
            isLoading.set(true)
            getAllNews()
        } else {
            error.value = activity.resources.getString(R.string.connection_not_available)
        }

    }

    private fun getAllNews() {
        disposable.add(
            request.getCategoryAllNews("all")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    fun handleResponse(newsResponse: NewsResponse) {
        isLoading.set(false)
        newsListDao.insertNewsList(newsResponse.data)
        newsList.value = newsListDao.getNews()
    }

    fun handleError(throwable: Throwable) {
        isLoading.set(false)
        error.value = throwable.message
    }
}