package ch.rmy.android.http_shortcuts.activities.editor.response

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import ch.rmy.android.framework.extensions.context
import ch.rmy.android.framework.utils.localization.Localizable
import ch.rmy.android.framework.utils.localization.StringResLocalizable
import ch.rmy.android.framework.viewmodel.BaseViewModel
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.data.domains.shortcuts.TemporaryShortcutRepository
import ch.rmy.android.http_shortcuts.data.models.Shortcut
import ch.rmy.android.http_shortcuts.navigation.NavigationDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResponseViewModel
@Inject
constructor(
    application: Application,
    private val temporaryShortcutRepository: TemporaryShortcutRepository,
) : BaseViewModel<Unit, ResponseViewState>(application) {

    override suspend fun initialize(data: Unit): ResponseViewState {
        val shortcut = temporaryShortcutRepository.getTemporaryShortcut()
        val responseHandling = shortcut.responseHandling!!
        return ResponseViewState(
            successMessageHint = getSuccessMessageHint(shortcut),
            responseUiType = responseHandling.uiType,
            responseSuccessOutput = responseHandling.successOutput,
            responseFailureOutput = responseHandling.failureOutput,
            successMessage = responseHandling.successMessage,
            storeResponseIntoFile = responseHandling.storeDirectory != null,
            storeDirectory = responseHandling.storeDirectory?.toUri()?.getStoreDirectoryName(),
            storeFileName = responseHandling.storeFileName.orEmpty(),
            replaceFileIfExists = responseHandling.replaceFileIfExists,
        )
    }

    private fun getSuccessMessageHint(shortcut: Shortcut): Localizable =
        StringResLocalizable(
            R.string.executed,
            Localizable.create { context ->
                shortcut.name.ifEmpty { context.getString(R.string.shortcut_safe_name) }
            },
        )

    fun onResponseUiTypeChanged(responseUiType: String) = runAction {
        updateViewState {
            copy(responseUiType = responseUiType)
        }
        withProgressTracking {
            temporaryShortcutRepository.setResponseUiType(responseUiType)
        }
    }

    fun onResponseSuccessOutputChanged(responseSuccessOutput: String) = runAction {
        updateViewState {
            copy(responseSuccessOutput = responseSuccessOutput)
        }
        withProgressTracking {
            temporaryShortcutRepository.setResponseSuccessOutput(responseSuccessOutput)
        }
    }

    fun onResponseFailureOutputChanged(responseFailureOutput: String) = runAction {
        updateViewState {
            copy(responseFailureOutput = responseFailureOutput)
        }
        withProgressTracking {
            temporaryShortcutRepository.setResponseFailureOutput(responseFailureOutput)
        }
    }

    fun onSuccessMessageChanged(successMessage: String) = runAction {
        updateViewState {
            copy(successMessage = successMessage)
        }
        withProgressTracking {
            temporaryShortcutRepository.setResponseSuccessMessage(successMessage)
        }
    }

    fun onDisplaySettingsClicked() = runAction {
        navigate(NavigationDestination.ShortcutEditorResponseDisplay)
    }

    fun onStoreIntoFileCheckboxChanged(enabled: Boolean) = runAction {
        if (enabled == viewState.storeResponseIntoFile) {
            skipAction()
        }
        if (enabled) {
            emitEvent(ResponseEvent.PickDirectory)
        } else {
            updateViewState {
                copy(storeResponseIntoFile = false)
            }
            withProgressTracking {
                temporaryShortcutRepository.setStoreDirectory(null)
            }
        }
    }

    fun onStoreFileNameChanged(storeFileName: String) = runAction {
        updateViewState {
            copy(storeFileName = storeFileName)
        }
        withProgressTracking {
            temporaryShortcutRepository.setStoreFileName(storeFileName)
        }
    }

    fun onStoreFileDirectoryPicked(directoryUri: Uri?) = runAction {
        updateViewState {
            copy(
                storeResponseIntoFile = directoryUri != null,
                storeDirectory = directoryUri?.getStoreDirectoryName(),
            )
        }
        withProgressTracking {
            temporaryShortcutRepository.setStoreDirectory(directoryUri)
        }
    }

    fun onStoreFileOverwriteChanged(enabled: Boolean) = runAction {
        updateViewState {
            copy(replaceFileIfExists = enabled)
        }
        withProgressTracking {
            temporaryShortcutRepository.setStoreReplaceIfExists(enabled)
        }
    }

    fun onBackPressed() = runAction {
        waitForOperationsToFinish()
        closeScreen()
    }

    private fun Uri.getStoreDirectoryName(): String? =
        DocumentFile.fromTreeUri(context, this)?.name
}
