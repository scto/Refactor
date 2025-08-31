package com.github.scto.refactor.selfupdate

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import timber.log.Timber

internal class UpdateDownloader(private val context: Context) {

    fun downloadAndInstall(url: String, fileName: String) {
        Timber.d("Download wird gestartet für: $url")
        val destination =
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(fileName)
                .setDescription("Update wird heruntergeladen")
                .setDestinationUri(Uri.fromFile(destination))
                .setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        val onComplete =
            object : BroadcastReceiver() {
                override fun onReceive(ctxt: Context, intent: Intent) {
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == downloadId) {
                        Timber.i("Download abgeschlossen. Installation wird gestartet.")
                        // Wichtig: Den Receiver wieder abmelden, um Speicherlecks zu vermeiden
                        context.unregisterReceiver(this)
                        initiateInstall(destination)
                    }
                }
            }
        // Registriert den Receiver, um auf den Abschluss des Downloads zu warten
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                Context.RECEIVER_EXPORTED,
            )
        } else {
            context.registerReceiver(
                onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            )
        }
    }

    private fun initiateInstall(apkFile: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        // Erstellt eine content:// URI für die APK-Datei, die für andere Apps sicher freigegeben
        // werden kann
        val uri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(context, "${context.packageName}.provider", apkFile)
            } else {
                Uri.fromFile(apkFile)
            }

        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e, "Fehler beim Starten der APK-Installation.")
        }
    }
}
