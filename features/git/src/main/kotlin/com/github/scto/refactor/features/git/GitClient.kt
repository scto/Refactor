package com.github.scto.refactor.features.git

import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File

interface GitClient {
   /**
    * Clones a repository from a given URL into a specified directory.
    *
    * @param url The remote repository URL.
    * @param directory The local directory to clone into.
    * @param branch The specific branch to clone. If null, the default branch is used.
    * @param cloneSubmodules If true, initializes and clones submodules recursively.
    */
   suspend fun clone(
       url: String,
       directory: File,
       branch: String? = null,
       cloneSubmodules: Boolean = false
   )

   suspend fun checkout(branch: String, directory: File)
   suspend fun fetch(directory: File)
   suspend fun commit(message: String, directory: File)
   suspend fun pull(directory: File, credentialsProvider: UsernamePasswordCredentialsProvider? = null)
   suspend fun push(directory: File, credentialsProvider: UsernamePasswordCredentialsProvider)
   suspend fun branch(name: String, directory: File, create: Boolean = true): String?
}
