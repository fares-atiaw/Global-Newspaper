package com.example.newspaper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.newspaper.R
import com.example.newspaper.databinding.FragmentArticleBinding
import com.example.newspaper.databinding.FragmentSavedNewsBinding
import com.example.newspaper.ui.binding
import com.example.newspaper.utils.BaseFragment

class ArticleFragment : BaseFragment(R.layout.fragment_article) {

    lateinit var binding : FragmentArticleBinding
    val args : ArticleFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentArticleBinding.inflate(inflater)

        val data = args.anArticle
        binding.webView.apply {
//            settings.javaScriptEnabled = true
//            webViewClient = WebViewClient()
//            webViewClient = object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                    view?.loadUrl(data.url)
//                    return true
//                }
//            }
//            loadUrl("https://www.google.co.in/")
            try{
                webViewClient = WebViewClient()
                if (url != null) {
                    loadUrl(data.url)
                }
            }
            catch (e: Exception){
                loadUrl("https://www.google.co.in/")
                Toast.makeText(context, "There is a problem in the url", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }








    /*override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }*/
}