package com.github.scto.refactor.features.git

import org.eclipse.jgit.api.Git
import timber.log.Timber
import java.io.File

class JGitClient : GitClient {
    override suspend fun clone(url: String, directory: File) {
        Timber.tag("GitClient").d("Cloning $url into ${directory.absolutePath}")
        Git.cloneRepository().setURI(url).setDirectory(directory).call()
    }
    override suspend fun checkout(branch: String, directory: File) {
        /* Implement */
    }
    override suspend fun fetch(directory: File) {
        /* Implement */
    }
    override suspend fun commit(message: String, directory: File) {
        /* Implement */
    }
}
