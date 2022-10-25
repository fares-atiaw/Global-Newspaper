package com.example.newspaper.utils

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.newspaper.ui.MainActivity
import com.example.newspaper.ui.NewsViewModel

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    val viewModel by viewModels<NewsViewModel>({ requireActivity() })
//    val viewModel = (activity as MainActivity).viewModel	// scope covers this activity and what inside like fragment
}