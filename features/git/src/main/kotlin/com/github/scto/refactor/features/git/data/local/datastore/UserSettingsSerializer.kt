package com.github.scto.refactor.features.git.data.local.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer

import kotlinx.serialization.SerializationException

import java.io.InputStream
import java.io.OutputStream

import com.github.scto.refactor.features.git.UserSettings

object UserSettingsSerializer : Serializer<UserSettings> {
    override val defaultValue: UserSettings = UserSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSettings {
        try {
            return UserSettings.parseFrom(input)
        } catch (exception: SerializationException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        output.write(t.toByteArray())
    }
}