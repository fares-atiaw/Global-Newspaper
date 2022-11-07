package com.example.newspaper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newspaper.R
import com.example.newspaper.adapters.NewsAdapter
import com.example.newspaper.databinding.FragmentSearchNewsBinding
import com.example.newspaper.utils.BaseFragment
import com.example.newspaper.utils.Constraints
import com.example.newspaper.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : BaseFragment(R.layout.fragment_search_news) {

    lateinit var binding : FragmentSearchNewsBinding
    private val adapter = NewsAdapter()
    var job : Job? = null
    private lateinit var scrollListener : RecyclerView.OnScrollListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchNewsBinding.inflate(inflater)
        binding.rvSearchNews.adapter = adapter

        binding.etSearch.addTextChangedListener { text ->
            job?.cancel()
            job = MainScope().launch {
                delay(750)
                if (text.toString().isNotEmpty())
                    viewModel.searchForNews(text.toString())
            }
        }

        var isScrolling = false
        var isLastPage = false
        var isLoading = false

        scrollListener = object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.rvSearchNews.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemsCount = layoutManager.childCount
                val totalItemsCount = layoutManager.itemCount

                val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
                val isAtLastItem = (firstVisibleItemPosition + visibleItemsCount) >= totalItemsCount
                val isNotAtBeginning = firstVisibleItemPosition >= 0        //???? .. > 0
                val isTotalMoreThanVisible = totalItemsCount >= Constraints.QUERY_PAGE_SIZE
                val shouldPaginate = isNotLoadingAndNotLastPage
                        && isAtLastItem
                        && isNotAtBeginning
                        && isTotalMoreThanVisible
                        && isScrolling

                if(shouldPaginate){
                    viewModel.searchForNews(binding.etSearch.text.toString())
                    isScrolling = false
                }
            }
        }

        binding.rvSearchNews.apply {
            adapter = adapter
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }

        adapter.onClick {
            findNavController().navigate(SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(it))
        }

//        binding.rvSearchNews.adapter = adapter

        viewModel.searchNews.observe(viewLifecycleOwner) { resource ->
            when(resource){
                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    isLoading = false
                    resource.data?.let {
                        adapter.submitList(it.articles.toList())
                        val totalPaginationTimes = it.totalResults / Constraints.QUERY_PAGE_SIZE +1 +1
                        // The last page of the response will always be empty, so (+1)
                        isLastPage = viewModel.searchNewsPage == totalPaginationTimes

                        if(isLastPage)
                            binding.rvSearchNews.setPadding(0,0,0,0)
                    }
                }
                is Resource.Error -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    isLoading = false
                    Toast.makeText(context, "Error happened : ${resource.message}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE     //Resource.Loading
                    isLoading = true
                 }

            }

        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

}