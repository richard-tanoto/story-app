package com.richard.storyapp

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.richard.storyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
        setStartDestination()
    }

    private fun setStartDestination() {
        viewModel.token.observe(this) {
            navController.navInflater.inflate(R.navigation.main_navigation).apply {
                setStartDestination(if (it.isNotEmpty()) R.id.homeFragment else R.id.loginFragment)
                navController.graph = this
            }
            Handler(mainLooper).postDelayed(
                { splashScreen.setKeepOnScreenCondition { false } },
                300
            )

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }
}