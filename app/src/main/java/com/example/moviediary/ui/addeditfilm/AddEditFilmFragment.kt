package com.example.moviediary.ui.addeditfilm

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
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

    private val addEditFilmViewModel: AddEditFilmViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FilmEditingViewBinding.bind(view)

        binding.apply {
            addEditFilmNameView.setText(addEditFilmViewModel.filmName)
            addEditFilmImageView.load(addEditFilmViewModel.filmPoster)
            addEditFilmGenreView.setText(addEditFilmViewModel.filmGenre)
            addEditFilmDateView.setText(addEditFilmViewModel.film?.createdDateFormatted)
            ratingBar.numStars = addEditFilmViewModel.film?.rating ?: 0
            var prods: String = ""
            addEditFilmViewModel.producers?.forEach {
                prods += it.name + "\n"
            }
            addEditFilmProducerView.setText(prods)

            addEditFilmNameView.addTextChangedListener {
                addEditFilmViewModel.filmName = it.toString()
            }

            addEditFilmGenreView.addTextChangedListener {
                addEditFilmViewModel.filmGenre = it.toString()
            }

            addEditFilmDateView.addTextChangedListener {
                addEditFilmViewModel.filmDate = it.toString()
            }

            addEditFilmImageView.setOnClickListener {

            }

            spinnerView.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, itemSelected: View, selectedItemPosition: Int, selectedId: Long) {
                    addEditFilmViewModel.filmStatus = resources.getStringArray(R.array.status)[selectedItemPosition]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


            ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                addEditFilmViewModel.filmRating = rating.toInt()
            }

            fabAddNote.setOnClickListener {
                addEditFilmViewModel.onAddClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            addEditFilmViewModel.addEditFilmEvent.collect { event ->
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