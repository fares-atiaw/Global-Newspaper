package com.example.newspaper.adapters;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newspaper.data.Article
import com.example.newspaper.databinding.ItemPreview2Binding
import com.example.newspaper.databinding.ItemPreviewBinding

class NewsAdapter : ListAdapter<Article, NewsAdapter.MyViewHolder>(Diff_Article()) {

    private var clickListener: ((data :Article) -> Unit)? = null
    fun onClick(listener: (data :Article) -> Unit) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.fromInflating(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class MyViewHolder private constructor(private val binding: ItemPreview2Binding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Article, clickListener: ((data :Article) -> Unit)?) {
            binding.article = data
            binding.root.setOnClickListener {
                clickListener?.let {
                    it(data)
                }
            }
            binding.executePendingBindings()

            binding.tvDescription.visibility = if (data.description.isNullOrEmpty()) View.GONE
            else View.VISIBLE
            binding.tvPublishedAt.visibility = if (data.publishedAt.isNullOrEmpty()) View.GONE
            else View.VISIBLE
        }

        /** private methods**/


        /** static variable(s) or method(s)**/
        companion object {
            fun fromInflating(parent: ViewGroup): MyViewHolder {
                val binding: ItemPreview2Binding = ItemPreview2Binding.inflate(
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