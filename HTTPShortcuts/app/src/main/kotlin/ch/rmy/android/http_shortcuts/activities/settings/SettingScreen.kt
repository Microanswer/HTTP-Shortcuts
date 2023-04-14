package ch.rmy.android.http_shortcuts.activities.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.components.ScreenScope
import ch.rmy.android.http_shortcuts.components.SimpleScaffold
import ch.rmy.android.http_shortcuts.components.bindViewModel

@Composable
fun ScreenScope.SettingsScreen() {
    val (viewModel, state) = bindViewModel<SettingsViewState, SettingsViewModel>()

    SimpleScaffold(
        viewState = state,
        title = stringResource(R.string.title_settings),
    ) { viewState ->
        SettingsContent(
            privacySectionVisible = viewState.privacySectionVisible,
            quickSettingsTileButtonVisible = viewState.quickSettingsTileButtonVisible,
            batteryOptimizationButtonVisible = viewState.batteryOptimizationButtonVisible,
            allowOverlayButtonVisible = viewState.allowOverlayButtonVisible,
            allowXiaomiOverlayButtonVisible = viewState.allowXiaomiOverlayButtonVisible,
            selectedLanguage = viewState.selectedLanguage,
            selectedDarkModeOption = viewState.selectedDarkModeOption,
            selectedClickActionOption = viewState.selectedClickActionOption,
            crashReportingEnabled = viewState.crashReportingAllowed,
            onLanguageSelected = viewModel::onLanguageSelected,
            onDarkModeOptionSelected = viewModel::onDarkModeOptionSelected,
            onClickActionOptionSelected = viewModel::onClickActionOptionSelected,
            onChangeTitleButtonClicked = viewModel::onChangeTitleButtonClicked,
            onLockButtonClicked = viewModel::onLockButtonClicked,
            onQuickSettingsTileButtonClicked = viewModel::onQuickSettingsTileButtonClicked,
            onGlobalScriptingButtonClicked = viewModel::onGlobalScriptingButtonClicked,
            onCrashReportingChanged = viewModel::onCrashReportingChanged,
            onClearCookiesButtonClicked = viewModel::onClearCookiesButtonClicked,
            onAllowOverlayButtonClicked = viewModel::onAllowOverlayButtonClicked,
            onAllowXiaomiOverlayButtonClicked = viewModel::onAllowXiaomiOverlayButtonClicked,
            onBatteryOptimizationButtonClicked = viewModel::onBatteryOptimizationButtonClicked,
        )
    }

    SettingsDialogs(
        dialogState = state?.dialogState,
        onClearCookiesConfirmed = viewModel::onClearCookiesConfirmed,
        onLockConfirmed = viewModel::onLockConfirmed,
        onTitleChangeConfirmed = viewModel::onTitleChangeConfirmed,
        onDismissalRequested = viewModel::onDialogDismissalRequested,
    )
}
