package com.tuyennguyen.trivia_quiz.remote.responses

import com.google.gson.annotations.SerializedName
import com.tuyennguyen.trivia_quiz.entities.Category

data class CategoryResponse(
    @SerializedName("trivia_categories")
    val categories: List<Category>
)
