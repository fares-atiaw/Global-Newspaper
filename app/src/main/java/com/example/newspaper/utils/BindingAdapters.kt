package com.example.newspaper.utils

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newspaper.R
import com.example.newspaper.adapters.NewsAdapter
import com.example.newspaper.data.Article
import com.example.newspaper.data.NewsResponse

@BindingAdapter("sourceIcon")
fun formatSource(textView: TextView, text: String) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.source_format), text)
}

@BindingAdapter("listData")								/**  COOL  **/
fun bindList(rv : RecyclerView, data : List<Article>?) {
// Here, recyclerview's adapter just acting as an Adapter-Class to submit the given data. So, you need to attach an Adapter-Class to it later.
    data?.let {
        val myAdapter = rv.adapter as NewsAdapter
        myAdapter.submitList(data){
            rv.scrollToPosition(0)      // scroll the list to the top after the diffs are calculated and posted
        }
    }
//    Then in the related activity/fragment, assign/attach the adapter into the binding.recyclerview
}

@BindingAdapter("visibilityCases")
fun bindProgressBar(progressBar: ProgressBar, result : Resource<NewsResponse>)
{
    when(result){
       is Resource.Success ->
           progressBar.visibility = View.GONE
        is Resource.Error ->
            progressBar.visibility = View.GONE
        else ->
            progressBar.visibility = View.VISIBLE
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imageView : ImageView, imageUrl :String?)
{
    if(imageUrl == null){
        imageView.visibility = View.GONE
    }

    imageUrl?.let {
        val image = it.toUri().buildUpon().scheme("https").build()
        GlideApp.with(imageView.context)
            .load(image)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imageView)
    }
    /*
    Glide.with(imageView.context)
        .load(image)
        .apply(
            RequestOptions()
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image))
        .into(imageView)
     */

    /** Do this first**/
    /*// Glide
    implementation ("com.github.bumptech.glide:glide:4.13.2") {
        exclude group: "com.android.support"
    }
    kapt 'androidx.annotation:annotation:1.4.0'
    kapt 'com.github.bumptech.glide:compiler:4.13.2'
    implementation ("com.github.bumptech.glide:glide:4.13.2@aar") {
        transitive = true
    }*/
    /** plus **/
    /*import com.bumptech.glide.annotation.GlideModule
    import com.bumptech.glide.module.AppGlideModule

    @GlideModule
    class MyGlideApp : AppGlideModule()*/


}