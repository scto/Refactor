package com.github.scto.refactor.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = viewModel(),
    onOnboardingFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Wenn das Onboarding abgeschlossen ist, die Callback-Funktion aufrufen
    if (uiState.isFinished) {
        LaunchedEffect(Unit) {
            onOnboardingFinished()
        }
    }

    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })

    // UI-Events an das ViewModel senden, wenn sich die Seite ändert
    LaunchedEffect(pagerState.currentPage) {
        viewModel.handleEvent(OnboardingUiEvent.PageChanged(pagerState.currentPage))
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                OnboardingPageContent(page = onboardingPages[pageIndex])
            }

            PagerIndicator(
                pageCount = onboardingPages.size,
                currentPage = uiState.currentPage
            )

            OnboardingButtons(
                pagerState = pagerState,
                isLastPage = uiState.currentPage == onboardingPages.size - 1,
                onFinish = { viewModel.handleEvent(OnboardingUiEvent.CompleteOnboarding) }
            )
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.image),
            contentDescription = page.title,
            modifier = Modifier
                .size(250.dp)
                .padding(bottom = 32.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PagerIndicator(pageCount: Int, currentPage: Int) {
    Row(
        modifier = Modifier
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { iteration ->
            val color = if (currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingButtons(
    pagerState: PagerState,
    isLastPage: Boolean,
    onFinish: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // "Überspringen"-Button (optional)
        TextButton(
            onClick = onFinish,
            modifier = Modifier.weight(1f)
        ) {
            Text("Überspringen")
        }

        // "Weiter" / "Fertig"-Button
        Button(
            onClick = {
                if (isLastPage) {
                    onFinish()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(if (isLastPage) "Fertig" else "Weiter")
        }
    }
}