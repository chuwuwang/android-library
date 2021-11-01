package com.sea.library.common.lifecycle

import androidx.annotation.MainThread
import androidx.lifecycle.*

typealias EventMutableLiveData<T> = MutableLiveData< Event<T> >

typealias EventLiveData<T> = LiveData< Event<T> >

fun <T> EventMutableLiveData<T>.postEventValue(value: T) {
    val event = Event(value)
    postValue(event)
}

fun <T> EventMutableLiveData<T>.setEventValue(value: T) {
    val event = Event(value)
    setValue(event)
}

/**
 * 事件可被多个观察者消费, 且每个观察者 [viewModelStore] 仅能消费一次
 */
@MainThread
inline fun <T> EventLiveData<T>.observeSingleEvent(owner: LifecycleOwner, viewModelStore: ViewModelStore, crossinline onChanged: (T) -> Unit): Observer< Event<T> > {
    val wrappedObserver = Observer< Event<T> > { t ->
        val value = t.getContentIfNotHandled(viewModelStore)
        if (value != null) {
            onChanged.invoke(value)
        }
    }
    observe(owner, wrappedObserver)
    return wrappedObserver
}

/**
 * 整个事件只能被唯一观察者消费
 */
@MainThread
inline fun <T> EventLiveData<T>.observeSingleEvent(owner: LifecycleOwner, crossinline onChanged: (T) -> Unit): Observer< Event<T> > {
    val wrappedObserver = Observer< Event<T> > { t ->
        val value = t.getContentIfNotHandled()
        if (value != null) {
            onChanged.invoke(value)
        }
    }
    observe(owner, wrappedObserver)
    return wrappedObserver
}

/**
 * 不考虑粘性问题, 和 UI 数据一样, 每次都通知观察者
 */
@MainThread
inline fun <T> EventLiveData<T>.observeEvent(owner: LifecycleOwner, crossinline onChanged: (T) -> Unit): Observer< Event<T> > {
    val wrappedObserver = Observer< Event<T> > { t ->
        val value = t.peekContent()
        onChanged.invoke(value)
    }
    observe(owner, wrappedObserver)
    return wrappedObserver
}