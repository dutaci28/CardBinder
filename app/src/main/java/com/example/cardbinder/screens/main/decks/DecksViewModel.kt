package com.example.cardbinder.screens.main.decks

import androidx.lifecycle.ViewModel
import com.example.cardbinder.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DecksViewModel @Inject constructor(repository: Repository) : ViewModel() {
}