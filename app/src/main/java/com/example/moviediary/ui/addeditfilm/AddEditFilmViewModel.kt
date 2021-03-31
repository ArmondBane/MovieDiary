package com.example.moviediary.ui.addeditfilm

import android.graphics.Bitmap
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.moviediary.data.Film
import com.example.moviediary.data.FilmDao
import com.example.moviediary.data.Producer
import com.example.moviediary.data.ProducerDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class AddEditFilmViewModel @ViewModelInject constructor(
        private val filmDao: FilmDao,
        private val producerDao: ProducerDao,
        @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val film = state.get<Film>("film")
    val producers = state.get<Array<Producer>>("film")

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

    private val addEditNoteEventChannel = Channel<AddEditFilmEvent>()
    val addEditNoteEvent = addEditNoteEventChannel.receiveAsFlow()

    sealed class AddEditFilmEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditFilmEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditFilmEvent()
    }
}