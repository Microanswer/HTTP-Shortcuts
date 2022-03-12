package ch.rmy.android.http_shortcuts.activities.main.usecases

import ch.rmy.android.framework.viewmodel.viewstate.DialogState
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.activities.main.ShortcutListViewModel

class GetMoveOptionsDialogUseCase {

    operator fun invoke(shortcutId: String, viewModel: ShortcutListViewModel) =
        DialogState.create {
            item(R.string.action_enable_moving) {
                viewModel.onMoveModeOptionSelected()
            }
                .item(R.string.action_move_to_category) {
                    viewModel.onMoveToCategoryOptionSelected(shortcutId)
                }
                .build()
        }
}
