package com.prabhakaran.newsapp.view

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prabhakaran.newsapp.activity.ListActivity
import com.prabhakaran.newsapp.util.ConfigVariables.Companion.LIST_VM
import com.prabhakaran.newsapp.viewmodel.ListViewModel

class MyViewModelFactory(
    val activity: AppCompatActivity? = null,
    val TAG: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (TAG == LIST_VM)
            return activity?.let { ListViewModel(it) } as T
        else return null as T;
    }
}