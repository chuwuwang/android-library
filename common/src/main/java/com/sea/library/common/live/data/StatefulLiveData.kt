package com.sea.library.common.live.data

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

typealias StatefulLiveData<T> = LiveData< RequestState<T> >

typealias StatefulMutableLiveData<T> = MutableLiveData< RequestState<T> >

@MainThread
inline fun <T> StatefulLiveData<T>.observeState(owner: LifecycleOwner, init: ResultBuilder<T>.() -> Unit) {
    val result = ResultBuilder<T>().apply(init)
    observe(owner) { state ->
        when (state) {
            is RequestState.Loading -> result.onLading.invoke()
            is RequestState.Error -> result.onError(state.error)
            is RequestState.Success -> result.onSuccess(state.data)
        }
    }
}