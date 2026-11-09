
package com.example.juicetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.juicetracker.databinding.FragmentTrackerBinding
import com.example.juicetracker.ui.AppViewModelProvider
import com.example.juicetracker.ui.JuiceListAdapter
import com.example.juicetracker.ui.TrackerViewModel
import kotlinx.coroutines.launch

class TrackerFragment : Fragment() {

    private val viewModel by viewModels<TrackerViewModel> {
        AppViewModelProvider.Factory
    }


    private val adapter = JuiceListAdapter(
        onEdit = { drink ->
            EntryDialog.newInstance(drink.id)
                .show(childFragmentManager, "EditJuice")
        },
        onDelete = { drink ->
            viewModel.deleteJuice(drink)
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTrackerBinding.inflate(
            inflater,
            container,
            false
        ).root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val binding = FragmentTrackerBinding.bind(view)

        binding.recyclerView.adapter = adapter


        binding.fab.setOnClickListener {
            EntryDialog.newInstance()
                .show(childFragmentManager, "AddJuice")
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.juicesStream.collect { juices ->
                    adapter.submitList(juices)
                }
            }
        }
    }
}
