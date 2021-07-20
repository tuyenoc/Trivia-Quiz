package com.tuyennguyen.trivia_quiz.ui.home

import androidx.lifecycle.*
import com.tuyennguyen.trivia_quiz.entities.Category
import com.tuyennguyen.trivia_quiz.repositories.CategoryRepository
import com.tuyennguyen.trivia_quiz.repositories.UserRepository
import com.tuyennguyen.trivia_quiz.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class HomeViewModel(private val userRepository: UserRepository, private val categoryRepository: CategoryRepository) : ViewModel() {

    val categorySelected: MutableLiveData<Category> = MutableLiveData()

    fun getUser() = liveData(Dispatchers.IO) {
        userRepository.getUser(userRepository.getUserUid()!!).collect {
            emit(it)
        }
    }

    fun getCategories(): LiveData<Resource<List<Category>>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.inProgress(data = null))
            try {
                val categories = categoryRepository.getCategories()
                if (categories != null) {
                    emit(Resource.success(categories))
                } else {
                    emit(Resource.error(data = null, ""))
                }
            } catch (e: Exception) {
                emit(Resource.error(data = null, ""))
            }
        }
    }

    fun setCategorySelected(category: Category){
        categorySelected.value = category
    }

}

class HomeViewModelFactory(private val userRepository: UserRepository, private val categoryRepository: CategoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(userRepository, categoryRepository) as T
    }
}
