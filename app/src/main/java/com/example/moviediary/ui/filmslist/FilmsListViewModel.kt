package com.example.moviediary.ui.filmslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.moviediary.data.FilmDao
import com.example.moviediary.data.ProducerDao

class FilmsListViewModel @ViewModelInject constructor(
    private val filmDao: FilmDao,
    private val producerDao: ProducerDao
) : ViewModel() {

    val filmsList = filmDao.getFilmsList().asLiveData()
}