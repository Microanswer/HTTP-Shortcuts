package ch.rmy.android.http_shortcuts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.utils.ColorUtil.colorIntToHexString
import ch.rmy.android.http_shortcuts.utils.ColorUtil.hexStringToColorInt

private val UNSUPPORTED_CHARACTERS_REGEX = "[^A-Fa-f0-9]".toRegex()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerDialog(
    initialColor: Int = android.graphics.Color.BLACK,
    onColorSelected: (Int) -> Unit,
    extraContent: @Composable ColumnScope.(Int) -> Unit = {},
    onDismissRequested: () -> Unit,
) {
    // TODO: Improve cursor position in text field

    var color by rememberSaveable {
        mutableStateOf(initialColor)
    }
    var colorText by remember {
        mutableStateOf("")
    }
    LaunchedEffect(color) {
        colorText = "#${color.colorIntToHexString()}"
    }

    val textStyle = TextStyle(
        fontSize = FontSize.SMALL,
        fontFamily = FontFamily.Monospace,
        color = Color.White,
        textAlign = TextAlign.Center,
        shadow = Shadow(
            Color.Black.copy(0.8f),
            offset = Offset(3f, 3f),
            blurRadius = 3f,
        ),
    )

    AlertDialog(
        onDismissRequest = onDismissRequested,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.SMALL),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.MEDIUM)
            ) {
                ColorPicker(
                    color = color,
                    onColorChanged = {
                        color = it
                    },
                )

                extraContent(color)

                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(color),
                    ),
                    value = colorText,
                    onValueChange = { text ->
                        val newColor = text.replace(UNSUPPORTED_CHARACTERS_REGEX, "").uppercase().take(6)
                        colorText = "#$newColor"
                        newColor
                            .takeIf { it.length == 6 }
                            ?.hexStringToColorInt()
                            ?.let {
                                color = it
                            }
                    },
                    singleLine = true,
                    textStyle = textStyle,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onColorSelected(color)
                },
            ) {
                Text(stringResource(R.string.dialog_ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequested,
            ) {
                Text(stringResource(R.string.dialog_cancel))
            }
        },
    )
}
