package com.example.newspaper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.newspaper.R
import com.example.newspaper.adapters.NewsAdapter
import com.example.newspaper.databinding.FragmentSearchNewsBinding
import com.example.newspaper.utils.BaseFragment
import com.example.newspaper.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : BaseFragment(R.layout.fragment_search_news) {

    lateinit var binding : FragmentSearchNewsBinding
    private val adapter = NewsAdapter()
    var job : Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchNewsBinding.inflate(inflater)

        binding.etSearch.addTextChangedListener { text ->
            job?.cancel()
            job = MainScope().launch {
                delay(750)
                if (text.toString().isNotEmpty())
                    viewModel.searchForNews(text.toString())
            }
        }

        adapter.onClick {
            findNavController().navigate(SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(it))
        }

        binding.rvSearchNews.adapter = adapter

        viewModel.searchNews.observe(viewLifecycleOwner) { resource ->
            when(resource){
                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    resource.data.let {
                        adapter.submitList(it?.articles)
                    }
                }
                is Resource.Error -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    Toast.makeText(context, "Error happened : ${resource.message}", Toast.LENGTH_LONG).show()
                }
                else ->
                    binding.paginationProgressBar.visibility = View.VISIBLE     //Resource.Loading
            }

        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

}