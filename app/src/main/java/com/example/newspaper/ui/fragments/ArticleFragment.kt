package com.example.newspaper.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
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

        // TODO: Create different tables for cashing
        // TODO: If there is no internet

        binding.webView.apply {
            try{
                this.webViewClient = WebViewClient()
                    loadUrl(data.url)
            }
            catch (e: Exception){
                loadUrl("https://www.google.co.in/")
                Toast.makeText(context, "There is a problem in the url", Toast.LENGTH_LONG).show()
            }
        }

        binding.fab.setOnClickListener {
            viewModel.saveThisArticle(data)
            fader(binding.fab)
            binding.fab.isClickable = false

            Toast.makeText(context, "✅", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }


    private fun fader(view : View) {
        val animator = ObjectAnimator.ofFloat(view, View.ALPHA, 0.05f).apply {
            repeatMode = ObjectAnimator.REVERSE
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) { view.isEnabled = false }
                override fun onAnimationEnd(animation: Animator?) { view.isEnabled = true }
            })
        }
        animator.start()
    }

}