package com.example.newspaper.ui.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.newspaper.R
import com.example.newspaper.adapters.NewsAdapter
import com.example.newspaper.databinding.FragmentBreakingNewsBinding
import com.example.newspaper.utils.BaseFragment
import com.example.newspaper.utils.Resource

class BreakingNewsFragment : BaseFragment(R.layout.fragment_breaking_news) {

    lateinit var binding : FragmentBreakingNewsBinding
    private val adapter = NewsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBreakingNewsBinding.inflate(inflater)

        binding.x = viewModel
        binding.lifecycleOwner = this

        adapter.onClick {
            findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it))
        }

        binding.rvBreakingNews.adapter = adapter

        viewModel.breakingNews.observe(viewLifecycleOwner) { resource ->
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
                    binding.paginationProgressBar.visibility = View.VISIBLE
            }

        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

}