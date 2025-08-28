/*
 * Copyright 2025 Squircle CE contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blacksquircle.ui.application.update

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.blacksquircle.ui.internal.inappupdate.InAppUpdateImpl
import com.blacksquircle.ui.internal.provider.inappupdate.InAppUpdate

@Composable
internal fun rememberInAppUpdate(): InAppUpdate {
    val activity = LocalActivity.current
        ?: error("CompositionLocal LocalActivity not present")
    return remember { InAppUpdateImpl(activity) }
}