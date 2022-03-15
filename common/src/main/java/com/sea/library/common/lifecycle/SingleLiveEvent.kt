package com.sea.library.common.lifecycle

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * SingleLiveEvent is a lifecycle-aware observable that sends only new updates after subscription and it will call the observable only if there's an explicit call to setValue() or call().
 * Note that only one observer is going to be notified of changes.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending: AtomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val has = hasActiveObservers()
        if (has) {
            Log.w("SingleLiveEvent", "Multiple observers registered but only one will be notified of changes.")
        }
        // Observe the internal MutableLiveData
        super.observe(owner) {
            val compare = pending.compareAndSet(true, false)
            if (compare) {
                observer.onChanged(it)
            }
        }
    }

    @MainThread
    override fun setValue(value: T ? ) {
        pending.set(true)
        super.setValue(value)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

}