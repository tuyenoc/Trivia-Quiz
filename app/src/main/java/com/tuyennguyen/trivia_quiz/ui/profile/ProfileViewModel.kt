package com.tuyennguyen.trivia_quiz.ui.profile

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.tuyennguyen.trivia_quiz.entities.User
import com.tuyennguyen.trivia_quiz.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

enum class ProfileState {
    Initial, Edit, Success, Failure, InProgress
}

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    val profileState: MutableLiveData<ProfileState> = MutableLiveData()

    fun changeProfileState(state: ProfileState) {
        profileState.value = state
    }

    fun updateProfile(avatar: Bitmap, fullName: String, score: Long) = viewModelScope.launch {
        changeProfileState(ProfileState.InProgress)
        if (userRepository.updateProfile(avatar, fullName, score)) {
            changeProfileState(ProfileState.Success)
        } else {
            changeProfileState(ProfileState.Failure)
        }
    }

    fun getUser() = liveData(Dispatchers.IO) {
        userRepository.getUser(userRepository.getUserUid()!!).collect {
            emit(it)
        }
    }

    fun logout() {
        userRepository.logout()
    }
}

class ProfileViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(userRepository) as T
    }
}