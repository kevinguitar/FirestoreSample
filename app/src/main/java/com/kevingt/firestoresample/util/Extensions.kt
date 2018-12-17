package com.kevingt.firestoresample.util

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View
import com.google.firebase.firestore.DocumentChange
import com.kevingt.firestoresample.model.Thing

fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { this.value = initialValue }

fun View.setColor(@ColorRes colorRes: Int) { setBackgroundColor(ContextCompat.getColor(context, colorRes)) }

fun DocumentChange.toThing(): Thing = document.toObject(Thing::class.java).apply { id = document.id }