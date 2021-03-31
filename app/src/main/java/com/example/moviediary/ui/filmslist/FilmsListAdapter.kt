package com.example.moviediary.ui.filmslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviediary.data.Film
import com.example.moviediary.data.Producer
import com.example.moviediary.databinding.FilmItemFragmentBinding

class FilmsListAdapter(private val listener: OnItemClickListener)
    : ListAdapter<Film, FilmsListAdapter.FilmsListViewHolder>(DiffCallback()) {

    private var filmsList: List<Film> = ArrayList()
    private var producersList: MutableList<Array<Producer>> = ArrayList()

    fun setFilms(films: List<Film>) {
        this.filmsList = films
        this.filmsList.forEach{
            producersList.add(emptyArray())
        }
        submitList(this.filmsList)
    }

    fun setProducers(produsers: List<Producer>) {
        this.filmsList.forEachIndexed { index, element ->
            var mas = emptyArray<Producer>()
            produsers.forEach{
                if (element.id == it.film_id)
                    mas += it
            }
            producersList[index] = mas
        }
    }

    interface OnItemClickListener{
        fun onItemClick(film: Film, produsers: Array<Producer>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsListViewHolder {
        val binding = FilmItemFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmsListViewHolder(binding)
    }

    override fun getItemCount() = filmsList.size

    override fun onBindViewHolder(holder: FilmsListViewHolder, position: Int) {
        holder.bind(filmsList[position], producersList[position])
    }

    inner class FilmsListViewHolder(private val binding: FilmItemFragmentBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {

                        listener.onItemClick(filmsList[position], producersList[position])
                    }
                }
            }
        }

        fun bind(film: Film, produsers: Array<Producer>) = with(itemView) {
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