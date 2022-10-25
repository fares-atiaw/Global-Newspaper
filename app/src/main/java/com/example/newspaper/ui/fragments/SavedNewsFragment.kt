package com.example.newspaper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.newspaper.R
import com.example.newspaper.adapters.NewsAdapter
import com.example.newspaper.databinding.FragmentSavedNewsBinding
import com.example.newspaper.databinding.FragmentSearchNewsBinding
import com.example.newspaper.ui.NewsViewModel
import com.example.newspaper.utils.BaseFragment
import com.example.newspaper.utils.Resource

class SavedNewsFragment : BaseFragment(R.layout.fragment_saved_news){
    lateinit var binding : FragmentSavedNewsBinding
    private val adapter = NewsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSavedNewsBinding.inflate(inflater)

        adapter.onClick {
            val bundle = Bundle().apply {
                putSerializable("key", it)
            }
            findNavController().navigate(SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(it))
        }

        binding.rvSavedNews.adapter = adapter

        viewModel.searchNews.observe(viewLifecycleOwner) { resource ->
            when(resource){
                is Resource.Success -> {
//                    binding.paginationProgressBar.visibility = View.GONE
                    resource.data.let {
                        adapter.submitList(it?.articles)
                    }
                }
                is Resource.Error -> {
//                    binding.paginationProgressBar.visibility = View.GONE
                    Toast.makeText(context, "Error happened : ${resource.message}", Toast.LENGTH_LONG).show()
                }
                else ->{}
//                    binding.paginationProgressBar.visibility = View.VISIBLE
            }
        }

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}