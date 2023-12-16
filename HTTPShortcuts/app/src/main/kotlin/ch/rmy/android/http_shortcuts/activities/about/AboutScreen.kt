package ch.rmy.android.http_shortcuts.activities.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.components.ChangeLogDialog
import ch.rmy.android.http_shortcuts.components.SimpleScaffold
import ch.rmy.android.http_shortcuts.components.SinglePageBrowser
import ch.rmy.android.http_shortcuts.components.Spacing
import ch.rmy.android.http_shortcuts.components.bindViewModel


private const val CUSTOM_CHANGELOG_ASSET_URL = "file:///android_asset/customchangelog.html"

@Composable
fun AboutScreen() {
    val (viewModel, state) = bindViewModel<AboutViewState, AboutViewModel>()

    SimpleScaffold(
        viewState = state,
        title = stringResource(R.string.title_about),
    ) { viewState ->
        AboutContent(
            versionNumber = viewState.versionNumber,
            fDroidVisible = viewState.fDroidVisible,
            onCustomeChangeLogButtonClicked = viewModel::onCustomeChangeLogButtonClicked,
            onChangeLogButtonClicked = viewModel::onChangeLogButtonClicked,
            onDocumentationButtonClicked = viewModel::onDocumentationButtonClicked,
            onContactButtonClicked = viewModel::onContactButtonClicked,
            onTranslateButtonClicked = viewModel::onTranslateButtonClicked,
            onPlayStoreButtonClicked = viewModel::onPlayStoreButtonClicked,
            onFDroidButtonClicked = viewModel::onFDroidButtonClicked,
            onGithubButtonClicked = viewModel::onGithubButtonClicked,
            onDonateButtonClicked = viewModel::onDonateButtonClicked,
            onAcknowledgementButtonClicked = viewModel::onAcknowledgementButtonClicked,
            onPrivacyPolicyButtonClicked = viewModel::onPrivacyPolicyButtonClicked,
        )
    }

    if (state?.changeLogDialogVisible == true) {
        ChangeLogDialog(
            permanentlyHidden = state.changeLogDialogPermanentlyHidden,
            onPermanentlyHiddenChanged = viewModel::onChangeLogDialogPermanentlyHiddenChanged,
            onDismissRequested = viewModel::onDialogDismissalRequested,
        )
    }

    if (state?.customChangeLogDialogVisible == true) {

        AlertDialog(
                title = { Text(stringResource(R.string.settings_custom_changelog)) },
                text = {
                    Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(Spacing.SMALL),
                    ) {
                        SinglePageBrowser(
                                CUSTOM_CHANGELOG_ASSET_URL,
                                modifier = Modifier.weight(1f, fill = true)
                        )
                    }
                },
                onDismissRequest = viewModel::onCustomeChangeLogDialogHide,
                confirmButton = {
                    TextButton(
                            onClick = viewModel::onCustomeChangeLogDialogHide,
                    ) {
                        Text(stringResource(R.string.dialog_ok))
                    }
                }

        )
    }
}
