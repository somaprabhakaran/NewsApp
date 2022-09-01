package com.prabhakaran.newsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import com.prabhakaran.newsapp.R
import com.prabhakaran.newsapp.databinding.ActivityListBinding
import com.prabhakaran.newsapp.util.ConfigVariables
import com.prabhakaran.newsapp.view.MyViewModelFactory
import com.prabhakaran.newsapp.viewmodel.ListViewModel
import com.prabhakaran.newsapp.adapter.NewsListAdapter
import com.prabhakaran.newsapp.adapter.NewsLoadStateAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {

    private lateinit var listViewModel: ListViewModel
    private var compositeDisposable: CompositeDisposable? = null
    lateinit var adapter: NewsListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityListBinding>(this,R.layout.activity_list)
        compositeDisposable = CompositeDisposable()

        this.listViewModel = ViewModelProvider(this, MyViewModelFactory(activity = this,
            TAG = ConfigVariables.LIST_VM)
        ).get(ListViewModel::class.java)
        binding.viewModel = listViewModel

        //adapter set in recyclerview
        adapter = NewsListAdapter(this)
        binding.listRecycleview.adapter = adapter.withLoadStateFooter(NewsLoadStateAdapter())


        //Once db have data its updated
        lifecycleScope.launch{
            listViewModel.data.collect{
                adapter.submitData(it)
            }
        }


        //Edittext text change listener
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    listViewModel.data.collect {

                        adapter.submitData(if (s.toString().isEmpty()) it else it.filter { news ->
                            news.title.contains(s.toString(), true) or news.author.contains(
                                s.toString(),
                                true
                            )
                        })
                    }
                }
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