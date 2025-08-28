package com.github.scto.refactor

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RefactorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.i("Gemini API Key loaded: ${BuildConfig.GEMINI_API_KEY.isNotEmpty()}")

        // Beispiel: Update-Prüfung beim App-Start (könnte auch in einem ViewModel sein)
        // val updateManager = UpdateManager(this)
        // CoroutineScope(Dispatchers.IO).launch {
        //     updateManager.checkForUpdates("GITHUB_USER", "GITHUB_REPO")
        // }
    }
}
