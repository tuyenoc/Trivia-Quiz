package com.tuyennguyen.trivia_quiz

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tuyennguyen.trivia_quiz.remote.RetrofitClient
import com.tuyennguyen.trivia_quiz.repositories.CategoryRepository
import com.tuyennguyen.trivia_quiz.repositories.QuestionRepository
import com.tuyennguyen.trivia_quiz.repositories.UserRepository

class MyApplication : Application() {
    private val firebaseAuth by lazy {
        Firebase.auth
    }

    private val firestore by lazy {
        Firebase.firestore
    }

    private val firebaseStorage by lazy {
        Firebase.storage
    }

    private val apiService by lazy {
        RetrofitClient.apiService
    }

    val userRepository by lazy {
        UserRepository(firebaseAuth, firestore, firebaseStorage)
    }

    val categoryRepository by lazy {
        CategoryRepository(apiService)
    }

    val questionRepository by lazy {
        QuestionRepository(apiService)
    }
}