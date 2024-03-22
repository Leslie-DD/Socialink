package com.hnu.heshequ.launcher

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.githang.statusbar.StatusBarCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hnu.heshequ.MeetApplication
import com.hnu.heshequ.R
import com.hnu.heshequ.databinding.ActivityMain2Binding
import com.hnu.heshequ.launcher.model.MainViewModel

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MeetApplication.getInstance().addActivity(this)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        StatusBarCompat.setStatusBarColor(this, Color.WHITE)

        viewModel.initialize()
    }
}