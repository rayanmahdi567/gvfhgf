package com.example.juicetracker


import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.juicetracker.ui.AppViewModelProvider
import com.example.juicetracker.ui.EntryViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EntryDialog : DialogFragment() {

    private val viewModel by viewModels<EntryViewModel> {
        AppViewModelProvider.Factory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext()
        val juiceId = arguments?.getLong("juiceId") ?: 0L

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            val padding = (20 * resources.displayMetrics.density).toInt()
            setPadding(padding, padding, padding, padding)
        }

        val nameInput = EditText(context).apply {
            hint = "Juice name"
            setSingleLine(true)
        }

        val descriptionInput = EditText(context).apply {
            hint = "Description"
            minLines = 2
        }

        val colors = listOf(
            "Red", "Orange", "Yellow", "Green", "Blue", "Purple"
        )

        val colorSpinner = Spinner(context).apply {
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                colors
            )
        }

        val ratingBar = RatingBar(context).apply {
            numStars = 5
            stepSize = 1f
            rating = 3f
        }

        layout.addView(nameInput)
        layout.addView(descriptionInput)
        layout.addView(colorSpinner)

        layout.addView(
            ratingBar,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        if (juiceId > 0) {
            lifecycleScope.launch {
                val juice = viewModel.getJuiceStream(juiceId).first()
                juice?.let {
                    nameInput.setText(it.name)
                    descriptionInput.setText(it.description)
                    val index = colors.indexOfFirst { color ->
                        color.equals(it.color, ignoreCase = true)
                    }
                    if (index >= 0) colorSpinner.setSelection(index)
                    ratingBar.rating = it.rating.toFloat()
                }
            }
        }

        return AlertDialog.Builder(context)
            .setTitle(if (juiceId > 0) "Edit Juice" else "Add Juice")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val name = nameInput.text.toString().trim()

                if (name.isNotEmpty()) {
                    viewModel.saveJuice(
                        id = juiceId,
                        name = name,
                        description = descriptionInput.text.toString(),
                        color = colorSpinner.selectedItem.toString(),
                        rating = ratingBar.rating.toInt()
                    )
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    companion object {
        fun newInstance(juiceId: Long = 0L): EntryDialog {
            return EntryDialog().apply {
                arguments = Bundle().apply {
                    putLong("juiceId", juiceId)
                }
            }
        }
    }
}
