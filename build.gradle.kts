/*
 * Copyright 2025, S.C.T.O
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//import com.diffplug.gradle.spotless.SpotlessTask
//import com.ncorti.ktfmt.gradle.tasks.*

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
	alias(libs.plugins.kotlin.jvm) apply false
	alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
	alias(libs.plugins.room) apply false
	//alias(libs.plugins.detekt) apply false
    alias(libs.plugins.google.protobuf) apply false
    //alias(libs.plugins.kfmt)
	//alias(libs.plugins.buildkonfig) apply false
}

buildscript {
  dependencies { classpath("com.google.protobuf:protobuf-gradle-plugin:0.9.6") }
}

/*
ktfmt {
    // KotlinLang style - 4 space indentation - From kotlinlang.org/docs/coding-conventions.html
    kotlinLangStyle()

    // Breaks lines longer than maxWidth. Default 100.
    maxWidth.set(80)
    // blockIndent is the indent size used when a new block is opened, in spaces.
    blockIndent.set(8)
    // continuationIndent is the indent size used when a line is broken because it's too
    continuationIndent.set(8)
    // Whether ktfmt should remove imports that are not used.
    removeUnusedImports.set(false)
    // Whether ktfmt should automatically add/remove trailing commas.
    manageTrailingCommas.set(false)
}
*/

tasks.register<Delete>("clean") { delete(layout.buildDirectory) }