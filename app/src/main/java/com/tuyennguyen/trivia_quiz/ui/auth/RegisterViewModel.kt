package com.tuyennguyen.trivia_quiz.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tuyennguyen.trivia_quiz.repositories.UserRepository
import kotlinx.coroutines.launch

enum class RegisterState {
    Success, Failure, InProgress
}

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    val registerState: MutableLiveData<RegisterState> = MutableLiveData()

    fun register(email: String, password: String) = viewModelScope.launch {

        registerState.value = RegisterState.InProgress
        val authResult = userRepository.createUserWithEmailAndPassword(email, password)
        if (authResult != null) {
            registerState.value = RegisterState.Success
        } else {
            registerState.value = RegisterState.Failure
        }
    }
}

class RegisterViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterViewModel(userRepository) as T
    }

}