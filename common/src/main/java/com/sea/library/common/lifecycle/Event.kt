package com.sea.library.common.lifecycle

import androidx.lifecycle.ViewModelStore

/**
 * Event Class is used as a wrapper for data that is exposed via a LiveData that represents an event.
 * Using this Event class, multiple observers can observe an event.
 *
 * Use this EventObserver to remove some boilerplate code if you end up having lots of events.
 */
@Suppress("MemberVisibilityCanBePrivate")
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