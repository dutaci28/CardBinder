package com.dutaci28.cardbinder.screens.navigation

import androidx.lifecycle.ViewModel
import com.dutaci28.cardbinder.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {
    private val repo = repository
    val getRandomCard = repo.getRandomCard()

}