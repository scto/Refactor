package com.github.scto.refactor.features.git

import java.io.File

interface GitClient {
    suspend fun clone(url: String, directory: File)
    suspend fun checkout(branch: String, directory: File)
    suspend fun fetch(directory: File)
    suspend fun commit(message: String, directory: File)
}
