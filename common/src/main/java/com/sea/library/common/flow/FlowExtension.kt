package com.sea.library.common.flow

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

inline fun <reified T> Flow<T>.observeWithLifecycle(lifecycleOwner: LifecycleOwner, minActiveState: Lifecycle.State = Lifecycle.State.STARTED, noinline action: suspend (T) -> Unit): Job = lifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect(action)
}

inline fun <reified T> Flow<T>.observeWithLifecycle(fragment: Fragment, minActiveState: Lifecycle.State = Lifecycle.State.STARTED, noinline action: suspend (T) -> Unit): Job = fragment.viewLifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(fragment.viewLifecycleOwner.lifecycle, minActiveState).collect(action)
}