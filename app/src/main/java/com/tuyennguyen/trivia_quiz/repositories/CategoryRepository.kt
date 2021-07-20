package com.tuyennguyen.trivia_quiz.repositories

import com.tuyennguyen.trivia_quiz.entities.Category
import com.tuyennguyen.trivia_quiz.remote.ApiService
import com.tuyennguyen.trivia_quiz.remote.responses.CategoryResponse

class CategoryRepository(private val apiService: ApiService) {
    suspend fun getCategories(): List<Category>? {
        val response = apiService.getCategories()
        if (response.isSuccessful) {
            val categoryResponse: CategoryResponse? = response.body()
            categoryResponse?.let {
                return it.categories
            }
            return null
        }
        return null
    }
}