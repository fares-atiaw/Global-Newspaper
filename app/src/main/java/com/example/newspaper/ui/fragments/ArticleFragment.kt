package com.example.newspaper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newspaper.R
import com.example.newspaper.databinding.FragmentArticleBinding
import com.example.newspaper.databinding.FragmentSavedNewsBinding
import com.example.newspaper.ui.binding
import com.example.newspaper.utils.BaseFragment

class ArticleFragment : BaseFragment(R.layout.fragment_article) {

    lateinit var binding : FragmentArticleBinding
    val args : ArticleFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentArticleBinding.inflate(inflater)

        val data = args.anArticle
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(data.url)
        }

        return binding.root
    }








    /*override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }*/
}