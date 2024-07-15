//CardBinder - a Magic: The Gathering collector's app.
//Copyright (C) 2024 Catalin Duta
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.(https://github.com/dutaci28/CardBinder?tab=GPL-3.0-1-ov-file#readme).
//If not, see <https://www.gnu.org/licenses/>.

package com.dutaci28.cardbinder.screens.content.collection

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dutaci28.cardbinder.model.CardCollectionEntry
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
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
    private val itemsCollection =
        db.collection("collection-" + (Firebase.auth.currentUser?.email ?: ""))
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
                    for (dc in snapshot.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                val item = dc.document.toObject(CardCollectionEntry::class.java)
                                _collectionCards.add(item)
                            }

                            DocumentChange.Type.REMOVED -> {
                                val item = dc.document.toObject(CardCollectionEntry::class.java)
                                _collectionCards.remove(item)
                            }

                            else -> {}
                        }
                    }
                }
            }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun deleteCardFromCollection(id: String, onSuccess: () -> Unit = {}) {
        val query = itemsCollection.whereEqualTo("card.id", id)
        query.get().addOnSuccessListener {
            for (document in it) {
                val documentRef = document.reference
                deleteDocument(documentRef, onSuccess)
            }
        }.addOnFailureListener { exception ->
            Log.w("CARDS", "Error getting documents: ", exception)
        }
    }

    private fun deleteDocument(documentRef: DocumentReference, onSuccess: () -> Unit = {}) {
        documentRef.delete()
            .addOnSuccessListener {
                Log.d("CARDS", "Document successfully deleted!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w("CARDS", "Error deleting document", e)
            }
    }

}