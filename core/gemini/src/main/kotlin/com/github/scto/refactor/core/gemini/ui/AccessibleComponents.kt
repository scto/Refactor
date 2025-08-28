package com.github.scto.refactor.core.gemini.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/**
 * Ein barrierefreies Composable für eine Refactoring-Option.
 *
 * @param text Der beschreibende Text für die Option.
 * @param checked Ob die Checkbox aktuell aktiviert ist.
 * @param onCheckedChange Callback, der bei einer Statusänderung aufgerufen wird.
 * @param modifier Der Modifier für dieses Composable.
 */
@Composable
fun AccessibleRefactoringOption(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                // Macht die gesamte Zeile klickbar und verbessert die Zugänglichkeit.
                .toggleable(
                    value = checked,
                    onValueChange = onCheckedChange,
                    role = Role.Checkbox, // Definiert die semantische Rolle für Screenreader.
                )
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null, // Die Logik wird durch das toggleable-Modifier gehandhabt.
        )
        Text(text = text, modifier = Modifier.padding(start = 16.dp))
    }
}
