package com.asu1.quizzer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asu1.resources.ViewModelState

class QuizSolverViewModel: ViewModel() {
    private val _viewModelState = MutableLiveData(ViewModelState.IDLE)
    val viewModelState: LiveData<ViewModelState> get() = _viewModelState

}