package com.example.moviediary.ui.addeditfilm

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviediary.data.Film
import com.example.moviediary.data.FilmDao
import com.example.moviediary.data.Producer
import com.example.moviediary.data.ProducerDao
import com.example.moviediary.ui.ADD_FILM_RESULT_OK
import com.example.moviediary.ui.EDIT_FILM_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

class AddEditFilmViewModel @ViewModelInject constructor(
        private val filmDao: FilmDao,
        private val producerDao: ProducerDao,
        @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val film = state.get<Film>("film")
    val producers = state.get<Array<Producer>>("producers")

    var filmName = state.get<String>("filmName") ?: film?.name ?: ""
        set(value) {
            field = value
            state.set("filmName", value)
        }
    var filmGenre = state.get<String>("filmGenre") ?: film?.genre ?: ""
        set(value) {
            field = value
            state.set("filmGenre", value)
        }
    var filmDate = state.get<String>("filmDate") ?: film?.year_of_issue ?: ""
        set(value) {
            field = value
            state.set("filmDate", value)
        }
    var filmPoster = state.get<Bitmap>("filmPoster") ?: film?.poster
        set(value) {
            field = value
            state.set("filmPoster", value)
        }
    var filmStatus = state.get<String>("filmStatus") ?: film?.status ?: ""
        set(value) {
            field = value
            state.set("filmStatus", value)
        }
    var filmRating = state.get<Int>("filmRating") ?: film?.rating ?: ""
        set(value) {
            field = value
            state.set("filmRating", value)
        }

    private val addEditFilmEventChannel = Channel<AddEditFilmEvent>()
    val addEditFilmEvent = addEditFilmEventChannel.receiveAsFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAddClick() {
        if (filmName.isBlank() || filmGenre.isBlank()) {
            showInvalidInputMessage("Поле не может быть пустым")
            return
        }

        if (film != null) {
            val updatedFilm = film.copy(
                    name = filmName,
                    genre = filmGenre,
                    year_of_issue = LocalDateTime.parse(filmDate.toString()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    poster =  film.poster,
                    status = film.status,
                    rating = filmRating.toString().toInt())

            updatedFilm(updatedFilm)
        }
        else
        {
            val newFilm = Film(
                    name = filmName,
                    genre = filmGenre,
                    year_of_issue = LocalDateTime.parse(filmDate.toString()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    poster =  null,
                    status = "",
                    rating = filmRating.toString().toInt())

            createFilm(newFilm)
        }
    }

    private fun createFilm(newFilm: Film) = viewModelScope.launch {
        filmDao.insert(newFilm)
        addEditFilmEventChannel.send(AddEditFilmEvent.NavigateBackWithResult(ADD_FILM_RESULT_OK))
    }

    private fun updatedFilm(updatedFilm: Film) =viewModelScope.launch {
        filmDao.update(updatedFilm)
        addEditFilmEventChannel.send(AddEditFilmEvent.NavigateBackWithResult(EDIT_FILM_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditFilmEventChannel.send(AddEditFilmEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditFilmEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditFilmEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditFilmEvent()
    }
}