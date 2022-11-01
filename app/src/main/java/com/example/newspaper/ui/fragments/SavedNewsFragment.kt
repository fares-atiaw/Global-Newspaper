package com.example.newspaper.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newspaper.R
import com.example.newspaper.adapters.NewsAdapter
import com.example.newspaper.databinding.FragmentSavedNewsBinding
import com.example.newspaper.databinding.FragmentSearchNewsBinding
import com.example.newspaper.ui.NewsViewModel
import com.example.newspaper.utils.BaseFragment
import com.example.newspaper.utils.Resource
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : BaseFragment(R.layout.fragment_saved_news){
    lateinit var binding : FragmentSavedNewsBinding
    private val adapter = NewsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSavedNewsBinding.inflate(inflater)

        adapter.onClick {
            findNavController().navigate(SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(it))
        }

        binding.rvSavedNews.adapter = adapter

        viewModel.invalidateShowNoDataForSavedNews()

        viewModel.showNoData.observe(viewLifecycleOwner){
            if(it)
                binding.noDataIcon.visibility = View.VISIBLE
            else
                binding.noDataIcon.visibility = View.GONE
        }

        viewModel.savedNews.observe(viewLifecycleOwner){
            it.let {
                adapter.submitList(it)
            }
        }

        ItemTouchHelper(makeTouchHelperCallback()).run {
            attachToRecyclerView(binding.rvSavedNews)
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    private fun makeTouchHelperCallback(): ItemTouchHelper.SimpleCallback
    {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.currentList[position]

                viewModel.deleteThisArticle(article)

                Snackbar.make(binding.root, "Remove from the list", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveThisArticle(article)
                    }
                }.show()
            }
        }

        return itemTouchHelperCallback
    }
}