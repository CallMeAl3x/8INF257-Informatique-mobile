package ca.uqac.stories.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StoriesRadioButtom (
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Text(text = text, modifier = Modifier.padding(start = 4.dp))
    }
}