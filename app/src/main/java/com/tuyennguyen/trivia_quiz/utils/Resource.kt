package com.tuyennguyen.trivia_quiz.utils

data class Resource<out T>(val state: State, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(state = State.Success, data = data, message = null)

        fun <T> error(data: T?, message: String?): Resource<T> =
            Resource(state = State.Failure, data = data, message = message)

        fun <T> inProgress(data: T?): Resource<T> = Resource(state = State.InProgress, data = data, message = null)
    }
}
