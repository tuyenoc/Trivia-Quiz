package com.tuyennguyen.trivia_quiz.repositories

import com.tuyennguyen.trivia_quiz.entities.Question
import com.tuyennguyen.trivia_quiz.remote.ApiService
import com.tuyennguyen.trivia_quiz.remote.responses.QuestionResponse

class QuestionRepository(private val apiService: ApiService) {
    suspend fun getQuestions(categoryId: Int, difficult: String): List<Question>? {
        val response = apiService.getQuestions(categoryId, difficult)
        if (response.isSuccessful) {
            val questionResponse: QuestionResponse? = response.body()
            if (questionResponse != null && questionResponse.responseCode == 0) {
                return questionResponse.questions
            }
            return null
        }
        return null
    }
}