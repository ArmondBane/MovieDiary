package com.example.moviediary.ui.filmslist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.moviediary.data.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FilmsListViewModel @ViewModelInject constructor(
    private val filmDao: FilmDao,
    private val producerDao: ProducerDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val filmslistEventChannel = Channel<FilmsListEvent>()
    val filmsListEvent = filmslistEventChannel.receiveAsFlow()

    val filmsList = combine(searchQuery.asFlow(), preferencesFlow) { sQ, pF ->
        Pair(sQ, pF)
    }.flatMapLatest { (sQ, pF) ->
        filmDao.getFilmsList(sQ, pF.sortOrder)
    }.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onFilmItemSwiped(film: Film) = viewModelScope.launch {
        filmDao.delete(film)
        filmslistEventChannel.send(FilmsListEvent.ShowUndoDeleteNoteMessage(film))
    }

    fun onUndoDeleteClick(film: Film) = viewModelScope.launch  {
        filmDao.insert(film)
    }

    sealed class FilmsListEvent {
        object NavigateToAddFilmScreen : FilmsListEvent()
        data class ShowUndoDeleteNoteMessage(val film: Film) : FilmsListEvent()
        data class NavigateToEditFilmScreen(val film: Film, val produsers: Array<Producer>) : FilmsListEvent()
    }
}