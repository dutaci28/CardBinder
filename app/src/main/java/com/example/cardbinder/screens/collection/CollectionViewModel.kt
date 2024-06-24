package com.example.cardbinder.screens.collection

import androidx.lifecycle.ViewModel
import com.example.cardbinder.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(repository: Repository) : ViewModel() {
}