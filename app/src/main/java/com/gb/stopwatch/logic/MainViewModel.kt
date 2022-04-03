package com.gb.stopwatch.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.stopwatch.domain.AppState
import com.gb.stopwatch.framework.os.TimeSource
import kotlinx.coroutines.*

class MainViewModel(private val timeSource: TimeSource) : ViewModel() {

    private val _state = MutableLiveData<AppState>(AppState.Paused(0))
    val state: LiveData<AppState> get() = _state

    private val timerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun onStartPressed() {
        val elapsed = (state.value as? AppState.Paused)?.elapsedTime ?: return
        _state.value = AppState.Running(timeSource.getMilliseconds() - elapsed, elapsed)
        timerScope.launch {
            do {
                delay(20)
                val curTime = timeSource.getMilliseconds()
                val startTime = (state.value as? AppState.Running)?.startTime ?: break
                _state.value = AppState.Running(startTime, curTime - startTime)
            } while(isActive)
        }
    }

    fun onStopPressed() {
        _state.value = AppState.Paused(0)
        stopJob()
    }

    fun onPausePressed() {
        (state.value as? AppState.Running)?.let {
            _state.value = AppState.Paused(timeSource.getMilliseconds() - it.startTime)
            stopJob()
        }
    }

    private fun stopJob() {
        timerScope.coroutineContext.cancelChildren()
    }
}