package com.tuyennguyen.trivia_quiz.ui.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tuyennguyen.trivia_quiz.repositories.UserRepository
import kotlinx.coroutines.launch

enum class LoginStatus {
    Success, Failure, InProgress
}

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    val loginStatus: MutableLiveData<LoginStatus> = MutableLiveData()

    fun login(email: String, password: String) = viewModelScope.launch {
        loginStatus.value = LoginStatus.InProgress
        val authResult = userRepository.signInWithEmailAndPassword(email, password)
        if (authResult != null) {
            loginStatus.value = LoginStatus.Success
        } else {
            loginStatus.value = LoginStatus.Failure
        }

    }
}

class LoginViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(userRepository) as T
    }

}