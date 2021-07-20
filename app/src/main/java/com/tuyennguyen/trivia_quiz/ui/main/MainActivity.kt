package com.tuyennguyen.trivia_quiz.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tuyennguyen.trivia_quiz.MyApplication
import com.tuyennguyen.trivia_quiz.databinding.ActivityMainBinding
import com.tuyennguyen.trivia_quiz.ui.auth.LoginFragment
import com.tuyennguyen.trivia_quiz.ui.home.HomeFragment
import com.tuyennguyen.trivia_quiz.utils.Navigator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val userRepository = (application as MyApplication).userRepository
        if (userRepository.getUserUid() == null) {
            Navigator.push(supportFragmentManager, LoginFragment.newInstance(), LoginFragment.TAG)
        } else {
            Navigator.push(supportFragmentManager, HomeFragment.newInstance(), HomeFragment.TAG)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            Navigator.pop(supportFragmentManager)
        } else {
            finish()
        }
    }
}