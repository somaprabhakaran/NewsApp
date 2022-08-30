package com.prabhakaran.newsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.prabhakaran.newsapp.R
import com.prabhakaran.newsapp.adapter.NewsListAdapter
import com.prabhakaran.newsapp.databinding.ActivityListBinding
import com.prabhakaran.newsapp.util.ConfigVariables
import com.prabhakaran.newsapp.view.MyViewModelFactory
import com.prabhakaran.newsapp.viewmodel.ListViewModel
import io.reactivex.disposables.CompositeDisposable

class ListActivity : AppCompatActivity() {

    private lateinit var listViewModel: ListViewModel
    private var compositeDisposable: CompositeDisposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityListBinding>(this,R.layout.activity_list)
        compositeDisposable = CompositeDisposable()

        this.listViewModel = ViewModelProvider(this, MyViewModelFactory(activity = this,
            TAG = ConfigVariables.LIST_VM)
        ).get(ListViewModel::class.java)
        binding.viewModel = listViewModel

        var adapter : NewsListAdapter = NewsListAdapter(this,arrayListOf())
        binding.listRecycleview.adapter = adapter

        listViewModel.newsList.observe(this,{
            adapter.addItems(it)
        })
    }

    fun navigation(url:String){
        var intent = Intent(this@ListActivity, NewsActivity::class.java)
        intent.putExtra("Url",url)
        startActivity(intent)
    }
}