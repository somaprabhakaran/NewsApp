package com.prabhakaran.newsapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


class Utility {
    companion object {

        fun isNetworkAvailable(activity: Context): Boolean {
            return try {
                val connectivityManager =
                    activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val network = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))
            } catch (e: Exception) {
                false
            }
        }

        @JvmStatic
        @BindingAdapter("expandText")
        fun expandText(view: TextView, desc: String) {
            if (desc.length > 150) {
                view.text = Html.fromHtml(
                    desc.substring(
                        0,
                        150
                    ) + "<font color='blue'> <u>Read More</u></font>"
                )
            }

            view.setOnClickListener {

                if (view.text.endsWith("Read More")) {
                    view.text = desc
                } else {
                    if (desc.length > 150) {
                        view.setText(
                            Html.fromHtml(
                                desc.substring(
                                    0,
                                    150
                                ) + "<font color='blue'> <u>Read More</u></font>"
                            )
                        )
                    } else view.text = desc
                }
            }
        }

        //Set Image from url
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageViewResource(imageView: ImageView, url:String){
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
        }
    }
}