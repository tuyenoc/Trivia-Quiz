package com.tuyennguyen.trivia_quiz.entities

data class User(
    val fullName: String,
    val avatar: String,
    val score: Long
){
    override fun toString(): String {
        return "$fullName - $avatar - $score"
    }
}