package com.github.scto.refactor.features.git.crypto

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.ByteArrayOutputStream
import java.io.File

class CryptoManager(private val context: Context) {

    // Erstellt einen Hauptschlüssel, der sicher im Android Keystore gespeichert wird.
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    // Erstellt eine temporäre, verschlüsselte Datei für die Operationen.
    private val encryptedFile = EncryptedFile.Builder(
        File(context.cacheDir, "temp_credentials.bin"),
        context,
        masterKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    /**
     * Verschlüsselt einen Klartext-String in ein Byte-Array.
     */
    fun encrypt(plainText: String): ByteArray {
        // Schreibe den Klartext in die verschlüsselte Datei.
        encryptedFile.openFileOutput().use { outputStream ->
            outputStream.write(plainText.toByteArray(Charsets.UTF_8))
        }
        // Lese die verschlüsselten Bytes aus der Datei.
        return encryptedFile.openFileInput().readBytes()
    }

    /**
     * Entschlüsselt ein Byte-Array zurück in einen Klartext-String.
     */
    fun decrypt(encryptedBytes: ByteArray): String {
        // Schreibe die verschlüsselten Bytes in die Datei.
        encryptedFile.openFileOutput().use { outputStream ->
            outputStream.write(encryptedBytes)
        }
        // Lese und entschlüssele die Daten aus der Datei.
        val outputStream = ByteArrayOutputStream()
        encryptedFile.openFileInput().use { inputStream ->
            inputStream.copyTo(outputStream)
        }
        return String(outputStream.toByteArray(), Charsets.UTF_8)
    }
}
