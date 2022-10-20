package com.example.newspaper.adapters;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newspaper.data.Article
import com.example.newspaper.databinding.ItemPreviewBinding

class NewsAdapter(val clickListener: (data :Article) -> Unit) : ListAdapter<Article, NewsAdapter.MyViewHolder>(Diff_Article()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.fromInflating(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class MyViewHolder private constructor(private val binding: ItemPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Article, clickListener: (data :Article) -> Unit) {
            binding.article = data
            binding.root.setOnClickListener {
                clickListener(data)
            }
            binding.executePendingBindings()
        }

        /** private methods**/


        /** static variable(s) or method(s)**/
        companion object {
            fun fromInflating(parent: ViewGroup): MyViewHolder {
                val binding: ItemPreviewBinding = ItemPreviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MyViewHolder(binding)
            }
        }
    }

    class Diff_Article : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            (oldItem.url == newItem.url)

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
            (oldItem == newItem)
    }

}