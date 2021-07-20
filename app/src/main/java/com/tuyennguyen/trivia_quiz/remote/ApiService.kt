package com.tuyennguyen.trivia_quiz.remote

import com.tuyennguyen.trivia_quiz.remote.responses.CategoryResponse
import com.tuyennguyen.trivia_quiz.remote.responses.QuestionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api_category.php")
    suspend fun getCategories(): Response<CategoryResponse>

    @GET("api.php?amount=10")
    suspend fun getQuestions(@Query("category") categoryId: Int,
                             @Query("difficulty") difficult: String): Response<QuestionResponse>
}