package ch.rmy.android.http_shortcuts.activities.variables.editor.types.select

import android.app.Application
import ch.rmy.android.framework.extensions.attachTo
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.activities.variables.editor.types.BaseVariableTypeViewModel
import ch.rmy.android.http_shortcuts.data.domains.variables.VariableRepository
import ch.rmy.android.http_shortcuts.variables.types.SelectType

class SelectTypeViewModel(application: Application) : BaseVariableTypeViewModel<Unit, SelectTypeViewState>(application) {

    private val variableRepository = VariableRepository()

    override fun initViewState() = SelectTypeViewState(
        options = computeOptionList(),
        isMultiSelect = SelectType.isMultiSelect(variable),
        separator = SelectType.getSeparator(variable),
    )

    private fun computeOptionList() =
        variable.options?.map { OptionItem(it.id, it.labelOrValue) } ?: emptyList()

    override fun onVariableChanged() {
        updateViewState {
            copy(options = computeOptionList())
        }
    }

    override fun onInitialized() {
        super.onInitialized()
        variableRepository.getObservableVariables()
            .subscribe { variables ->
                updateViewState {
                    copy(variables = variables)
                }
            }
            .attachTo(destroyer)
    }

    fun onOptionClicked(id: String) {
        val option = variable.options?.firstOrNull { it.id == id } ?: return
        emitEvent(
            SelectTypeEvent.ShowEditDialog(
                optionId = id,
                label = option.label,
                value = option.value,
            )
        )
    }

    fun onOptionMoved(optionId1: String, optionId2: String) {
        performOperation(
            temporaryVariableRepository.moveOption(optionId1, optionId2)
        )
    }

    override fun validate() =
        if (variable.options.isNullOrEmpty()) {
            showSnackbar(R.string.error_not_enough_select_values)
            false
        } else true

    fun onAddButtonClicked() {
        emitEvent(SelectTypeEvent.ShowAddDialog)
    }

    fun onMultilineChanged(enabled: Boolean) {
        updateViewState {
            copy(isMultiSelect = enabled)
        }
        saveData()
    }

    fun onSeparatorChanged(separator: String) {
        updateViewState {
            copy(separator = separator)
        }
        saveData()
    }

    fun onAddDialogConfirmed(label: String, value: String) {
        performOperation(
            temporaryVariableRepository.addOption(label, value)
        )
    }

    fun onEditDialogConfirmed(optionId: String, label: String, value: String) {
        performOperation(
            temporaryVariableRepository.updateOption(optionId, label, value)
        )
    }

    fun onDeleteOptionSelected(optionId: String) {
        performOperation(
            temporaryVariableRepository.removeOption(optionId)
        )
    }

    private fun saveData() {
        performOperation(
            temporaryVariableRepository.setDataForType(
                mapOf(
                    SelectType.KEY_MULTI_SELECT to currentViewState.isMultiSelect.toString(),
                    SelectType.KEY_SEPARATOR to currentViewState.separator,
                )
            )
        )
    }
}