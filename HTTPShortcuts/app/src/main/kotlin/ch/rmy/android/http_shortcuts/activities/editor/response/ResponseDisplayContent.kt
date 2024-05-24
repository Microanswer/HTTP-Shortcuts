package ch.rmy.android.http_shortcuts.activities.editor.response

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.components.Checkbox
import ch.rmy.android.http_shortcuts.components.SelectionField
import ch.rmy.android.http_shortcuts.components.SettingsButton
import ch.rmy.android.http_shortcuts.components.Spacing
import ch.rmy.android.http_shortcuts.data.enums.ResponseContentType
import ch.rmy.android.http_shortcuts.data.enums.ResponseDisplayAction
import ch.rmy.android.http_shortcuts.data.models.ResponseHandling
import java.nio.charset.Charset

@Composable
fun ResponseDisplayContent(
    responseUiType: String,
    responseSuccessOutput: String,
    responseContentType: ResponseContentType?,
    responseCharset: Charset?,
    availableCharsets: List<Charset>,
    includeMetaInformation: Boolean,
    responseDisplayActions: List<ResponseDisplayAction>,
    useMonospaceFont: Boolean,
    fontSize: Int?,
    onResponseContentTypeChanged: (ResponseContentType?) -> Unit,
    onResponseCharsetChanged: (Charset?) -> Unit,
    onDialogActionChanged: (ResponseDisplayAction?) -> Unit,
    onIncludeMetaInformationChanged: (Boolean) -> Unit,
    onWindowActionsButtonClicked: () -> Unit,
    onUseMonospaceFontChanged: (Boolean) -> Unit,
    onFontSizeChanged: (Int?) -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = Spacing.MEDIUM)
    ) {
        SelectionField(
            modifier = Modifier
                .padding(
                    horizontal = Spacing.MEDIUM,
                ),
            title = if (responseSuccessOutput == ResponseHandling.SUCCESS_OUTPUT_MESSAGE) {
                stringResource(R.string.label_response_message_type_selection)
            } else {
                stringResource(R.string.label_response_type_selection)
            },
            selectedKey = responseContentType,
            items = RESPONSE_CONTENT_TYPES.toItems(),
            onItemSelected = onResponseContentTypeChanged,
        )

        if (responseSuccessOutput == ResponseHandling.SUCCESS_OUTPUT_RESPONSE) {
            Spacer(modifier = Modifier.height(Spacing.SMALL))

            SelectionField(
                modifier = Modifier.padding(horizontal = Spacing.MEDIUM),
                title = stringResource(R.string.label_response_charset),
                selectedKey = responseCharset,
                items = listOf(
                    null to stringResource(R.string.option_response_charset_auto)
                ) + availableCharsets.map {
                    it to it.name()
                },
                onItemSelected = onResponseCharsetChanged,
            )
        }

        when (responseUiType) {
            ResponseHandling.UI_TYPE_DIALOG -> {
                SelectionField(
                    modifier = Modifier
                        .padding(top = Spacing.SMALL)
                        .padding(horizontal = Spacing.MEDIUM),
                    title = stringResource(R.string.label_dialog_action_dropdown),
                    selectedKey = responseDisplayActions.firstOrNull(),
                    items = DIALOG_ACTIONS.toItems(),
                    onItemSelected = onDialogActionChanged,
                )

                AnimatedVisibility(visible = responseContentType != ResponseContentType.HTML) {
                    FontSizeSelection(
                        fontSize = fontSize,
                        onFontSizeChanged = onFontSizeChanged,
                    )
                }

                AnimatedVisibility(visible = responseContentType == ResponseContentType.PLAIN_TEXT) {
                    Checkbox(
                        label = stringResource(R.string.label_monospace_response),
                        checked = useMonospaceFont,
                        onCheckedChange = onUseMonospaceFontChanged,
                    )
                }
            }
            ResponseHandling.UI_TYPE_WINDOW -> {
                SettingsButton(
                    title = stringResource(R.string.button_select_response_toolbar_buttons),
                    subtitle = pluralStringResource(
                        R.plurals.subtitle_response_toolbar_actions,
                        count = responseDisplayActions.size,
                        responseDisplayActions.size,
                    ),
                    onClick = onWindowActionsButtonClicked,
                )

                Checkbox(
                    label = stringResource(R.string.label_include_meta_information),
                    subtitle = stringResource(R.string.subtitle_include_meta_information),
                    checked = includeMetaInformation,
                    onCheckedChange = onIncludeMetaInformationChanged,
                )

                AnimatedVisibility(visible = responseContentType != ResponseContentType.HTML) {
                    FontSizeSelection(
                        fontSize = fontSize,
                        onFontSizeChanged = onFontSizeChanged,
                    )
                }

                AnimatedVisibility(visible = responseContentType == ResponseContentType.PLAIN_TEXT) {
                    Checkbox(
                        label = stringResource(R.string.label_monospace_response),
                        checked = useMonospaceFont,
                        onCheckedChange = onUseMonospaceFontChanged,
                    )
                }
            }
        }
    }
}

@Composable
private fun FontSizeSelection(
    fontSize: Int?,
    onFontSizeChanged: (Int?) -> Unit,
) {
    SelectionField(
        modifier = Modifier
            .padding(top = Spacing.SMALL)
            .padding(horizontal = Spacing.MEDIUM),
        title = stringResource(R.string.label_font_size),
        selectedKey = fontSize,
        items = FONT_SIZES.toItems(),
        itemFactory = { key, value ->
            Text(
                text = value,
                fontSize = key?.sp ?: TextUnit.Unspecified,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        onItemSelected = onFontSizeChanged,
    )
}

private val DIALOG_ACTIONS = listOf(
    null to R.string.label_dialog_action_none,
    ResponseDisplayAction.RERUN to R.string.action_rerun_shortcut,
    ResponseDisplayAction.SHARE to R.string.share_button,
    ResponseDisplayAction.COPY to R.string.action_copy_response,
)

private val RESPONSE_CONTENT_TYPES = listOf(
    null to R.string.option_response_content_type_auto,
    ResponseContentType.PLAIN_TEXT to R.string.option_response_content_type_plain_text,
    ResponseContentType.JSON to R.string.option_response_content_type_json,
    ResponseContentType.XML to R.string.option_response_content_type_xml,
    ResponseContentType.HTML to R.string.option_response_content_type_html,
)

private val FONT_SIZES = listOf(
    8 to R.string.font_size_tiny,
    11 to R.string.font_size_very_small,
    13 to R.string.font_size_small,
    null to R.string.font_size_regular,
    19 to R.string.font_size_large,
    23 to R.string.font_size_very_large,
    27 to R.string.font_size_huge,
)

@Composable
private fun <T> List<Pair<T, Int>>.toItems() =
    map { (value, label) -> value to stringResource(label) }