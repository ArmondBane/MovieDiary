package com.example.moviediary.ui.filmslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviediary.R
import com.example.moviediary.data.Film
import com.example.moviediary.databinding.FilmsListFragmentBinding
import com.example.moviediary.ui.filmslist.FilmsListAdapter.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmsListFragment : Fragment(R.layout.films_list_fragment),  OnItemClickListener{

    private val filmsListViewModel: FilmsListViewModel by viewModels()

    private val filmsListAdapter: FilmsListAdapter = FilmsListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FilmsListFragmentBinding.bind(view)

        binding.apply {
            filmsList.apply {
                adapter = filmsListAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        filmsListViewModel.filmsList.observe(viewLifecycleOwner) {
            filmsListAdapter.setFilms(it)
        }

    }

    override fun onItemClick(film: Film) {
        TODO("Not yet implemented")
    }
}