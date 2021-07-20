package com.tuyennguyen.trivia_quiz.remote.responses


import com.google.gson.annotations.SerializedName
import com.tuyennguyen.trivia_quiz.entities.Question

data class QuestionResponse(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val questions: List<Question>
)