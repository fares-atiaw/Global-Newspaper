package com.example.newspaper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newspaper.R
import com.example.newspaper.adapters.NewsAdapter
import com.example.newspaper.databinding.FragmentBreakingNewsBinding
import com.example.newspaper.utils.BaseFragment
import com.example.newspaper.utils.Constraints.QUERY_PAGE_SIZE
import com.example.newspaper.utils.Resource

class BreakingNewsFragment : BaseFragment(R.layout.fragment_breaking_news) {

    lateinit var binding : FragmentBreakingNewsBinding
    private val adapter = NewsAdapter()
    private lateinit var scrollListener : RecyclerView.OnScrollListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBreakingNewsBinding.inflate(inflater)

        binding.x = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.rvBreakingNews.adapter = adapter

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
                val layoutManager = binding.rvBreakingNews.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemsCount = layoutManager.childCount
                val totalItemsCount = layoutManager.itemCount

                val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
                val isAtLastItem = (firstVisibleItemPosition + visibleItemsCount) >= totalItemsCount
                val isNotAtBeginning = firstVisibleItemPosition >= 0        //???? .. > 0
                val isTotalMoreThanVisible = totalItemsCount >= QUERY_PAGE_SIZE
                val shouldPaginate = isNotLoadingAndNotLastPage
                        && isAtLastItem
                        && isNotAtBeginning
                        && isTotalMoreThanVisible
                        && isScrolling

                if(shouldPaginate){
                    viewModel.getBreakingNews("eg")
                    isScrolling = false
                }
                else
                    binding.rvBreakingNews.setPadding(0,0,0,0)

            }
        }

        binding.rvBreakingNews.apply {
            adapter = adapter
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }

        adapter.onClick {
            findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it))
        }

        viewModel.breakingNews.observe(viewLifecycleOwner) { resource ->
            when(resource){
                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    isLoading = false
                    resource.data?.let {
                        adapter.submitList(it.articles.toList())
                        val totalPaginationTimes = it.totalResults / QUERY_PAGE_SIZE +1 +1
                        // The last page of the response will always be empty, so (+1)
                        isLastPage = viewModel.breakingNewsPage == totalPaginationTimes

                        if(isLastPage)
                            binding.rvBreakingNews.setPadding(0,0,0,0)
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