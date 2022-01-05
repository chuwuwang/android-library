package com.sea.library.common.flow

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

@PublishedApi
internal class ObserverWhileStartedImpl<T>(lifecycleOwner: LifecycleOwner, private val flow: Flow<T>, private val collector: suspend (T) -> Unit) : DefaultLifecycleObserver {

    private var job: Job ? = null

    override fun onStart(owner: LifecycleOwner) {
        job = owner.lifecycleScope.launchWhenStarted {
            flow.collect {
                collector(it)
            }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        val jb = job
        if (jb != null) {
            jb.cancel()
            job = null
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

}

inline fun <reified T> Flow<T>.collectWhileStarted(lifecycleOwner: LifecycleOwner, noinline collector: suspend (T) -> Unit) {
    ObserverWhileStartedImpl(lifecycleOwner, this, collector)
}

inline fun <reified T> Flow<T>.collectWhileStartedIn(lifecycleOwner: LifecycleOwner) {
    ObserverWhileStartedImpl(lifecycleOwner, this, {})
}

@PublishedApi
internal class ObserverWhileResumedImpl<T>(lifecycleOwner: LifecycleOwner, private val flow: Flow<T>, private val collector: suspend (T) -> Unit) : DefaultLifecycleObserver {

    private var job: Job ? = null

    override fun onResume(owner: LifecycleOwner) {
        job = owner.lifecycleScope.launchWhenResumed {
            flow.collect {
                collector(it)
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        val jb = job
        if (jb != null) {
            jb.cancel()
            job = null
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

}

inline fun <reified T> Flow<T>.collectWhileResumed(lifecycleOwner: LifecycleOwner, noinline collector: suspend (T) -> Unit, ) {
    ObserverWhileResumedImpl(lifecycleOwner, this, collector)
}

inline fun <reified T> Flow<T>.collectWhileResumedIn(lifecycleOwner: LifecycleOwner) {
    ObserverWhileResumedImpl(lifecycleOwner, this, {})
}