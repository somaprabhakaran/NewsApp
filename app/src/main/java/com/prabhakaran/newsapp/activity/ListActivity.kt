package com.prabhakaran.newsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.prabhakaran.newsapp.R
import com.prabhakaran.newsapp.adapter.NewsListAdapter
import com.prabhakaran.newsapp.dao.NewsListDAO
import com.prabhakaran.newsapp.database.AppDatabase
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

        //adapter set in recyclerview
        val adapter : NewsListAdapter = NewsListAdapter(this,arrayListOf())
        binding.listRecycleview.adapter = adapter

        //Once db have data its updated
        listViewModel.newsList.observe(this) {
            adapter.addItems(it)
        }

        //Edittext text change listener
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                listViewModel.getSearchableList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


    }

    //This function is used to navigate to news details screen
    fun navigation(url:String){
        val intent = Intent(this@ListActivity, NewsActivity::class.java)
        intent.putExtra("Url",url)
        startActivity(intent)
    }
}