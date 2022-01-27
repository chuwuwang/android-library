package com.sea.library.common.flow

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap

@Suppress("UNCHECKED_CAST")
object SharedFlowBus {

    private var events = ConcurrentHashMap< Any, MutableSharedFlow<Any> >()
    private var stickyEvents = ConcurrentHashMap< Any, MutableSharedFlow<Any> >()

    fun <T> with(objectKey: Class<T>): MutableSharedFlow<T> {
        val contains = events.containsKey(objectKey)
        if ( ! contains ) {
            events[objectKey] = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
        }
        return events[objectKey] as MutableSharedFlow<T>
    }

    fun <T> withSticky(objectKey: Class<T>): MutableSharedFlow<T> {
        val contains = stickyEvents.containsKey(objectKey)
        if ( ! contains ) {
            stickyEvents[objectKey] = MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
        }
        return stickyEvents[objectKey] as MutableSharedFlow<T>
    }

    fun <T> on(objectKey: Class<T>): LiveData<T> {
        return with(objectKey).asLiveData()
    }

    fun <T> onSticky(objectKey: Class<T>): LiveData<T> {
        return withSticky(objectKey).asLiveData()
    }

}