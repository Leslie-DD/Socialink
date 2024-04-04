package com.leslie.socialink.launcher

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leslie.socialink.R
import com.leslie.socialink.SocialinkApplication
import com.leslie.socialink.databinding.ActivityMainBinding
import com.leslie.socialink.launcher.model.MainViewModel
import com.leslie.socialink.utils.StatusBarUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SocialinkApplication.getInstance().addActivity(this)

        StatusBarUtil.hackInStatusBar(this@MainActivity)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        viewModel.initialize()
    }

    companion object {
        private const val TAG = "[MainActivity]"
    }

}