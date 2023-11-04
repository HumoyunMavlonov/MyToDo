package uz.gita.mytodo.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp


@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    placeholder: String = "Type here",
    value: String,
    onValueChange: (String) -> Unit,
    isClickable: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null
) {

    OutlinedTextField(
        value = value,
        enabled = isClickable,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        label = { Text(text = placeholder) },
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        trailingIcon = trailingIcon,
        singleLine = true,
    )
}
