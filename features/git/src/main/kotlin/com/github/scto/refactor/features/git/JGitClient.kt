package com.github.scto.refactor.features.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import timber.log.Timber
import java.io.File

class JGitClient : GitClient {
   override suspend fun clone(
       url: String,
       directory: File,
       branch: String?,
       cloneSubmodules: Boolean
   ) {
       Timber.tag("GitClient")
           .d("Cloning $url into ${directory.absolutePath} | Branch: ${branch ?: "default"} | Submodules: $cloneSubmodules")
       try {
           val cloneCommand = Git.cloneRepository()
               .setURI(url)
               .setDirectory(directory)
               .setCloneSubmodules(cloneSubmodules) // Set submodule cloning option

           // Set the specific branch if provided
           if (!branch.isNullOrBlank()) {
               cloneCommand.setBranch(branch)
           }

           cloneCommand.call()
           Timber.tag("GitClient").i("Repository cloned successfully.")
       } catch (e: GitAPIException) {
           Timber.tag("GitClient").e(e, "Failed to clone repository")
       }
   }

   override suspend fun checkout(branch: String, directory: File) {
       Timber.tag("GitClient").d("Checking out branch '$branch' in ${directory.absolutePath}")
       try {
           Git.open(directory).use { git ->
               git.checkout().setName(branch).call()
               Timber.tag("GitClient").i("Branch '$branch' checked out successfully.")
           }
       } catch (e: Exception) {
           Timber.tag("GitClient").e(e, "Failed to checkout branch '$branch'")
       }
   }

   override suspend fun fetch(directory: File) {
       Timber.tag("GitClient").d("Fetching from remote in ${directory.absolutePath}")
       try {
           Git.open(directory).use { git ->
               git.fetch().call()
               Timber.tag("GitClient").i("Fetch successful.")
           }
       } catch (e: Exception) {
           Timber.tag("GitClient").e(e, "Failed to fetch from remote")
       }
   }

   override suspend fun commit(message: String, directory: File) {
       Timber.tag("GitClient").d("Committing with message: '$message'")
       try {
           Git.open(directory).use { git ->
               git.commit().setMessage(message).call()
               Timber.tag("GitClient").i("Commit successful.")
           }
       } catch (e: Exception) {
           Timber.tag("GitClient").e(e, "Failed to commit changes")
       }
   }

   /**
    * Pulls the latest changes from the remote repository.
    */
   override suspend fun pull(directory: File, credentialsProvider: UsernamePasswordCredentialsProvider?) {
       Timber.tag("GitClient").d("Pulling changes in ${directory.absolutePath}")
       try {
           Git.open(directory).use { git ->
               val pullCommand = git.pull()
               credentialsProvider?.let { pullCommand.setCredentialsProvider(it) }
               val result = pullCommand.call()

               if (result.isSuccessful) {
                   Timber.tag("GitClient").i("Pull successful.")
               } else {
                   Timber.tag("GitClient").w("Pull not successful. Merge status: ${result.mergeResult.mergeStatus}")
               }
           }
       } catch (e: Exception) {
           Timber.tag("GitClient").e(e, "Failed to pull changes")
       }
   }

   /**
    * Pushes committed changes to the remote repository.
    * Requires authentication.
    */
   override suspend fun push(directory: File, credentialsProvider: UsernamePasswordCredentialsProvider) {
       Timber.tag("GitClient").d("Pushing changes from ${directory.absolutePath}")
       try {
           Git.open(directory).use { git ->
               val pushCommand = git.push().setCredentialsProvider(credentialsProvider)
               val results = pushCommand.call()
               results.forEach { result ->
                   result.remoteUpdates.forEach { update ->
                       Timber.tag("GitClient").i("Push status: ${update.status} to ${result.remoteName}")
                   }
               }
           }
       } catch (e: Exception) {
           Timber.tag("GitClient").e(e, "Failed to push changes")
       }
   }

   /**
    * Creates a new branch or returns the current branch name.
    */
   override suspend fun branch(name: String, directory: File, create: Boolean): String? {
       return try {
           Git.open(directory).use { git ->
               if (create) {
                   Timber.tag("GitClient").d("Creating branch '$name'")
                   git.branchCreate().setName(name).call()
                   Timber.tag("GitClient").i("Branch '$name' created.")
               }
               git.repository.branch
           }
       } catch (e: Exception) {
           Timber.tag("GitClient").e(e, "Failed to perform branch operation")
           null
       }
   }
}
