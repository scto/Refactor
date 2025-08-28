package com.github.scto.refactor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

import com.github.scto.refactor.ui.home.HomeScreen
import com.github.scto.refactor.ui.onboarding.OnboardingScreen
import com.github.scto.refactor.ui.theme.RefactorTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val onboardingCompleted by viewModel.onboardingCompleted.collectAsState()
			
            RefactorTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    if (onboardingCompleted) {
                        HomeScreen()
                    } else {
                        OnboardingScreen(onOnboardingFinished = {
                            viewModel.setOnboardingCompleted(true)
                        })
                    }
                }
            }
        }
    }
}