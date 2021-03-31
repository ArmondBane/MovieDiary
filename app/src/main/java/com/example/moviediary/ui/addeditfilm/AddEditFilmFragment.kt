package com.example.moviediary.ui.addeditfilm

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.moviediary.R
import com.example.moviediary.databinding.FilmEditingViewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditFilmFragment : Fragment(R.layout.film_editing_view) {

    private val addEditNoteViewModel: AddEditFilmViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FilmEditingViewBinding.bind(view)

        binding.apply {
            addEditFilmNameView.setText(addEditNoteViewModel.filmName)
            addEditFilmImageView.load(addEditNoteViewModel.filmPoster)
            addEditFilmGenreView.setText(addEditNoteViewModel.filmGenre)

            fabAddNote.setOnClickListener {

            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            addEditNoteViewModel.addEditNoteEvent.collect { event ->
                when (event) {
                    is AddEditFilmViewModel.AddEditFilmEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditFilmViewModel.AddEditFilmEvent.NavigateBackWithResult -> {
                        setFragmentResult("add_edit_request",
                                bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}