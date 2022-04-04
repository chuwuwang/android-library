package com.sea.library.common.extension

import android.annotation.SuppressLint
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.ViewGroupUtils

@SuppressLint("RestrictedApi")
fun View.expandClickArea(dx: Int, dy: Int) {
    class MultiTouchDelegate(bound: Rect ? = null, delegateView: View) : TouchDelegate(bound, delegateView) {

        private var delegateView: View ? = null

        val delegateViewMap = mutableMapOf<View, Rect>()

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x.toInt()
            val y = event.y.toInt()
            var handled = false
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    delegateView = findDelegateViewUnder(x, y)
                }
                MotionEvent.ACTION_CANCEL -> {
                    delegateView = null
                }
            }
            val delegate = delegateView
            if (delegate != null) {
                event.setLocation(delegate.width / 2f, delegate.height / 2f)
                handled = delegate.dispatchTouchEvent(event)
            }
            return handled
        }

        private fun findDelegateViewUnder(x: Int, y: Int): View ? {
            delegateViewMap.forEach { entry ->
                val contains = entry.value.contains(x, y)
                if (contains) return entry.key
            }
            return null
        }

    }
    if (parent == null) return
    val parentView = parent as ViewGroup
    if (parentView.touchDelegate == null) {
        parentView.touchDelegate = MultiTouchDelegate(delegateView = this)
    }
    post {
        val rect = Rect()
        ViewGroupUtils.getDescendantRect(parentView, this, rect)
        rect.inset(-dx, -dy)
        if (parentView.touchDelegate != null) {
            val multiTouchDelegate = parentView.touchDelegate as MultiTouchDelegate
            multiTouchDelegate.delegateViewMap[this] = rect
        }
    }
}

inline fun TextView.addTextChangedListenerClosure(
    crossinline afterTextChanged: (Editable ? ) -> Unit = { },
    crossinline onTextChanged: (CharSequence ?, Int, Int, Int) -> Unit = { _, _, _, _ -> },
    crossinline beforeTextChanged: (CharSequence ?, Int, Int, Int) -> Unit = { _, _, _, _ -> }
) {
    val listener = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence ?, start: Int, count: Int, after: Int) {
            beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence ?, start: Int, before: Int, count: Int) {
            onTextChanged(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable ? ) {
            afterTextChanged(s)
        }

    }
    this.addTextChangedListener(listener)
}