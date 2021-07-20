package com.tuyennguyen.trivia_quiz.ui.play

import androidx.lifecycle.*
import com.tuyennguyen.trivia_quiz.entities.Question
import com.tuyennguyen.trivia_quiz.repositories.QuestionRepository
import kotlinx.coroutines.launch

enum class PlayState {
    InProgress, Failure, Play, Great, Wrong, Lose, Finished
}

class PlayViewModel(
    private val questionRepository: QuestionRepository,
    private val categoryId: Int,
    private val difficult: String
) :
    ViewModel() {

    val playState: MutableLiveData<PlayState> = MutableLiveData()
    var questions: List<Question>? = null
    var currentQuestion: MutableLiveData<Question> = MutableLiveData()
    val index: MutableLiveData<Int> = MutableLiveData()
    val score: MutableLiveData<Long> = MutableLiveData()
    val live: MutableLiveData<Int> = MutableLiveData()

    init {
        index.value = 0
        score.value = 0
        live.value = 3
        getQuestions()
    }

    fun getQuestions() = viewModelScope.launch {
        playState.postValue(PlayState.InProgress)
        try {
            val questions = questionRepository.getQuestions(categoryId, difficult.lowercase())
            if (questions != null) {
                this@PlayViewModel.questions = questions
                currentQuestion.postValue(questions[index.value!!])
                playState.postValue(PlayState.Play)
            } else {
                playState.postValue(PlayState.Failure)
            }
        } catch (e: Exception) {
            playState.postValue(PlayState.Failure)
        }
    }

    fun answer(answer: String) {
        if (answer == currentQuestion.value!!.correctAnswer) {
            playState.value = PlayState.Great
            score.value = score.value!! + 1
            if (index.value!! < questions!!.size - 1) {
                index.value = index.value!! + 1
                currentQuestion.value = questions!![index.value!!]
                playState.value = PlayState.Play
            } else {
                playState.value = PlayState.Finished
            }
        } else {
            if (live.value == 1) {
                live.value = 0
                playState.value = PlayState.Lose
            } else {
                live.value = live.value!! - 1
                playState.value = PlayState.Wrong
            }
        }
    }
}

class PlayViewModelFactory(
    private val questionRepository: QuestionRepository,
    private val categoryId: Int,
    private val difficult: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayViewModel(questionRepository, categoryId, difficult) as T
    }
}