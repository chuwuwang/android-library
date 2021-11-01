package com.sea.library.common.lifecycle

import androidx.lifecycle.ViewModelStore

open class Event<out T>(private val data: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    private var map = HashMap<ViewModelStore, Boolean>()

    fun getContentIfNotHandled(): T ? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }

    fun getContentIfNotHandled(viewModelStore: ViewModelStore): T ? {
        val contains = map.contains(viewModelStore)
        return if (contains) {
            null
        } else {
            map[viewModelStore] = true
            data
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = data

}