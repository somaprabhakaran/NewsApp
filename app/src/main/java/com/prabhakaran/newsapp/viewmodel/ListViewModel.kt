package com.prabhakaran.newsapp.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.prabhakaran.newsapp.R
import com.prabhakaran.newsapp.activity.ListActivity
import com.prabhakaran.newsapp.adapter.NewsPagingSource
import com.prabhakaran.newsapp.dao.NewsListDAO
import com.prabhakaran.newsapp.database.AppDatabase
import com.prabhakaran.newsapp.model.NewsResponse
import com.prabhakaran.newsapp.rest.ApiClient
import com.prabhakaran.newsapp.rest.ApiService
import com.prabhakaran.newsapp.util.LoadingScreen
import com.prabhakaran.newsapp.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ListViewModel(val activity: AppCompatActivity) : ViewModel() {
    private var request: ApiService = ApiClient.buildService(ApiService::class.java)
    private val error = MutableLiveData<String>()
    private val disposable = CompositeDisposable()
    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var appDatabase = AppDatabase.getDatabase(activity)
    val newsListDao: NewsListDAO =appDatabase.newsListDao()
    var data = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        ),
    ) { NewsPagingSource(newsListDao)}.flow

    init {
        if (appDatabase.newsListDao().getNews().isEmpty()) {
            //api call
            getNews()
        }
    }

    fun getNews() {

        if (Utility.isNetworkAvailable(activity)) {
            LoadingScreen.displayLoadingWithText(activity, "Please wait...", false)
            isLoading.set(true)
            getAllNews()
        } else {
            error.value = activity.resources.getString(R.string.connection_not_available)
            LoadingScreen.hideLoading()
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
        LoadingScreen.hideLoading()
        newsListDao.insertNewsList(newsResponse.data)

        if (activity is ListActivity) {
            (activity as ListActivity).adapter.refresh()
        }

    }

    fun handleError(throwable: Throwable) {
        isLoading.set(false)
        LoadingScreen.hideLoading()
        error.value = throwable.message
    }
}