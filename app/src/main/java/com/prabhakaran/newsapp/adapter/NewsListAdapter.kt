package com.prabhakaran.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prabhakaran.newsapp.activity.ListActivity
import com.prabhakaran.newsapp.databinding.ListItemBinding
import com.prabhakaran.newsapp.model.News

class NewsListAdapter(var activity: ListActivity, var listdata: List<News>) :
    RecyclerView.Adapter<NewsListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listdata.get(position))
    }

    override fun getItemCount(): Int {
        return listdata.size
    }




    fun addItems(postItems: List<News>) {
        listdata=postItems
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: News) {
            binding.model = item

            binding.wholeView.setOnClickListener {
                activity.navigation(item.url);
            }
        }
    }
}