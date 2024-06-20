package com.example.cardbinder.screens.search

import androidx.lifecycle.ViewModel
import com.example.cardbinder.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    repository: Repository
): ViewModel() {
    val getAllCards = repository.getAllCards()
}