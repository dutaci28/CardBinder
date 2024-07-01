package com.example.cardbinder.screens.main.collection

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardbinder.model.CardCollectionEntry
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor() : ViewModel() {

    private val _collectionCards = mutableStateListOf<CardCollectionEntry>()
    val collectionCards = _collectionCards
    private val _collectionViewToggle = mutableStateOf(false)
    val collectionViewToggle = _collectionViewToggle

    private val db = Firebase.firestore
    private val itemsCollection = db.collection("collection-" + (Firebase.auth.currentUser?.email ?: ""))
    private var listener = ListenerRegistration {}

    init {
        cardsCollectionListener()
    }

    private fun cardsCollectionListener() = viewModelScope.launch {
        listener =
            itemsCollection.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle errors
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    _collectionCards.clear()
                    for (dc in snapshot.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val item = dc.document.toObject(CardCollectionEntry::class.java)
                            _collectionCards.add(item)
                        }
                    }
                }
            }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}