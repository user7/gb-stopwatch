package com.gb.stopwatch.framework.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gb.stopwatch.databinding.ActivityMainBinding
import com.gb.stopwatch.domain.AppState
import com.gb.stopwatch.logic.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel by inject<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPause.setOnClickListener { viewModel.onPausePressed() }
        binding.buttonStart.setOnClickListener { viewModel.onStartPressed() }
        binding.buttonStop.setOnClickListener { viewModel.onStopPressed() }

        viewModel.state.observe(this) { renderState(it) }
    }

    private fun renderState(state: AppState) {
        val elapsed = when (state) {
            is AppState.Paused -> state.elapsedTime
            is AppState.Running -> state.elapsedTime
        }
        val mils = elapsed % 1000
        val secs = elapsed / 1000 % 60
        val mins = elapsed / 1000 / 60 % 60
        val hours = elapsed / 1000 / 60 / 60
        val text =
            if (hours > 0)
                "%d:%02d:%02d.%03d".format(hours, mins, secs, mils)
            else
                "%02d:%02d.%03d".format(mins, secs, mils)
        binding.textTime.text = text
    }
}