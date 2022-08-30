package com.prabhakaran.newsapp.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prabhakaran.newsapp.R
import com.prabhakaran.newsapp.dao.NewsListDAO
import com.prabhakaran.newsapp.database.AppDatabase
import com.prabhakaran.newsapp.model.News
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
    var newsList: MutableLiveData<List<News>> = MutableLiveData()
    private val disposable = CompositeDisposable()
    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var tempList: MutableList<News> = mutableListOf()

    lateinit var newsListDao: NewsListDAO
    var appDatabase = AppDatabase.getDatabase(activity)

    init {
        newsListDao = appDatabase.newsListDao()

        if (newsListDao.getNews().isEmpty()) {
            //api call
            getNews()
        }else{
            //get from local
            tempList = newsListDao.getNews()
            newsList.value = tempList
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

    //Search
    fun getSearchableList(s: String?) {
        if (s == null || s.isEmpty()) {
                newsList.value = tempList
        }else{
            newsList.value = tempList.filter {
                it.title.contains(s.toString(),true)
            }
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
        tempList = newsListDao.getNews()
        newsList.value = tempList

    }

    fun handleError(throwable: Throwable) {
        isLoading.set(false)
        LoadingScreen.hideLoading()
        error.value = throwable.message
    }
}