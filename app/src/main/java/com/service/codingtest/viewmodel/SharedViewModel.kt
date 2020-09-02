package com.service.codingtest.viewmodel

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel

/* unused */
class SharedViewModel : ViewModel() {
    private val liveText = MutableLiveData<String>()
    val text: LiveData<String>
        get() = liveText

    fun setText(text: String) {
        liveText.value = text
    }
}