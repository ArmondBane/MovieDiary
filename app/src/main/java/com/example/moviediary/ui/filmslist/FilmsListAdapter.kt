package com.example.moviediary.ui.filmslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviediary.data.Film
import com.example.moviediary.databinding.FilmItemFragmentBinding
import com.example.moviediary.databinding.FilmsListFragmentBinding
import kotlinx.android.synthetic.main.film_item_fragment.view.*

class FilmsListAdapter(private val listener: OnItemClickListener)
    : ListAdapter<Film, FilmsListAdapter.FilmsListViewHolder>(DiffCallback()) {

    private var filmsList: List<Film> = ArrayList()

    fun setFilms(films: List<Film>) {
        this.filmsList = films
        submitList(this.filmsList)
    }

    interface OnItemClickListener{
        fun onItemClick(film: Film)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsListViewHolder {
        val binding = FilmItemFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmsListViewHolder(binding)
    }

    override fun getItemCount() = filmsList.size

    override fun onBindViewHolder(holder: FilmsListViewHolder, position: Int) {
        holder.bind(filmsList[position])
    }

    inner class FilmsListViewHolder(private val binding: FilmItemFragmentBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        //TODO Click Listener action
                    }
                }
            }
        }

        fun bind(film: Film) = with(itemView) {
            binding.apply {
                nameTextView.text = film.name
                genreTextView.text = film.genre
                statusTextView.text = film.status
                posterImageView.load(film.poster)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Film, newItem: Film) =
            oldItem == newItem
    }
}