package com.example.newspaper.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newspaper.R
import com.example.newspaper.databinding.ActivityMainBinding

lateinit var binding : ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navController = binding.myNavHostFragment.getFragment<NavHostFragment>().navController

        binding.bottomNavigationView.setupWithNavController(navController)

    }
}
// 9f8642e7503f43cb872ad0be9bc86b37