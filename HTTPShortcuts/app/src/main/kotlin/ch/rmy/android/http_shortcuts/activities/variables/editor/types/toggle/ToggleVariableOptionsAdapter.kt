package ch.rmy.android.http_shortcuts.activities.variables.editor.types.toggle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.rmy.android.framework.ui.BaseAdapter
import ch.rmy.android.http_shortcuts.databinding.ToggleOptionBinding
import ch.rmy.android.http_shortcuts.variables.VariablePlaceholderProvider
import ch.rmy.android.http_shortcuts.variables.Variables
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ToggleVariableOptionsAdapter : BaseAdapter<OptionItem>() {

    lateinit var variablePlaceholderProvider: VariablePlaceholderProvider

    sealed interface UserEvent {
        data class OptionClicked(val id: String) : UserEvent
    }

    private val userEventSubject = PublishSubject.create<UserEvent>()

    val userEvents: Observable<UserEvent>
        get() = userEventSubject

    override fun createViewHolder(viewType: Int, parent: ViewGroup, layoutInflater: LayoutInflater) =
        SelectOptionViewHolder(ToggleOptionBinding.inflate(layoutInflater, parent, false))

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, position: Int, item: OptionItem) {
        (holder as SelectOptionViewHolder).setItem(item)
    }

    override fun areItemsTheSame(oldItem: OptionItem, newItem: OptionItem) =
        oldItem.id == newItem.id

    inner class SelectOptionViewHolder(
        private val binding: ToggleOptionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var optionId: String
            private set

        init {
            binding.root.setOnClickListener {
                userEventSubject.onNext(UserEvent.OptionClicked(optionId))
            }
        }

        fun setItem(item: OptionItem) {
            optionId = item.id
            binding.toggleOptionValue.text = Variables.rawPlaceholdersToVariableSpans(item.text, variablePlaceholderProvider, 0)
        }
    }
}