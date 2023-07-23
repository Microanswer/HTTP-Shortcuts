package ch.rmy.android.http_shortcuts.activities.settings

import androidx.compose.runtime.Stable

@Stable
sealed class SettingsDialogState {
    data class ChangeTitle(val oldTitle: String) : SettingsDialogState()
    data class ChangeUserAgent(val oldUserAgent: String) : SettingsDialogState()
    object LockApp : SettingsDialogState()
    object ClearCookies : SettingsDialogState()
}
