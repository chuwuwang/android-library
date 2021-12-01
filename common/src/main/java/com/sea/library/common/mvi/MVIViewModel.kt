package com.sea.library.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class MVIViewModel<State : UiState, Event : UiEvent, Effect : UiEffect> : ViewModel() {

    /**
     * 初始状态
     * stateFlow 区别于 LiveData 必须有初始值
     */
    private val initialState: State by lazy { createInitialState() }

    /**
     * uiState 聚合页面的全部 UI 状态
     */
    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    /**
     * Event 包含用户与 UI 的交互（如点击操作）, 也有来自后台的消息（如切换自习模式）
     */
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    /**
     * Effect 用作事件带来的副作用, 通常是一次性事件且一对一的订阅关系
     * 例如: 弹Toast, 导航Fragment等
     */
    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    abstract fun createInitialState(): State

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    protected abstract fun handleEvent(event: Event)

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    protected fun setState(reduce: () -> State) {
        val newState = reduce()
        _uiState.value = newState
    }

    protected fun setEffect(builder: () -> Effect) {
        val newEffect = builder()
        viewModelScope.launch {
            _effect.send(newEffect)
        }
    }

}

