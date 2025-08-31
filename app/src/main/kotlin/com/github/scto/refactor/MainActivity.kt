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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource

import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint

import se.warting.inappupdate.compose.material.MaterialRequireLatestVersion

import com.github.scto.refactor.R
import com.github.scto.refactor.ui.home.HomeScreen
import com.github.scto.refactor.ui.onboarding.OnboardingScreen
import com.github.scto.refactor.ui.theme.RefactorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val onboardingCompleted by viewModel.onboardingCompleted.collectAsState()

            RefactorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
					MaterialRequireLatestVersion {
                        Welcome()
                    }
                    if (onboardingCompleted) {
                        HomeScreen()
                    } else {
                        OnboardingScreen(
                            onOnboardingFinished = { viewModel.setOnboardingCompleted(true) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Welcome() {
	Text(stringResource(id = R.string.update_message))
}