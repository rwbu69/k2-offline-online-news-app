package com.example.k2offlineonlinenewsapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.k2offlineonlinenewsapp.R
import com.example.k2offlineonlinenewsapp.data.local.NewsEntity
import com.example.k2offlineonlinenewsapp.databinding.ItemNewsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter : ListAdapter<NewsEntity, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    class MyViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsEntity) {
            binding.tvItemTitle.text = news.title
            binding.tvItemDescription.text = news.description
            binding.tvSourceName.text = news.sourceName
            binding.tvItemPublishedDate.text = formatDate(news.publishedAt)

            Glide.with(itemView.context)
                .load(news.urlToImage)
                .placeholder(R.drawable.background_anime_placeholder)
                .error(R.drawable.background_anime_placeholder)
                .into(binding.imgPoster)
        }

        private fun formatDate(dateString: String?): String {
            if (dateString.isNullOrEmpty()) return "Tanggal Kosong"
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
                val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("id", "ID"))
                val date = inputFormat.parse(dateString)
                if (date != null) outputFormat.format(date) else dateString
            } catch (e: Exception) {
                dateString
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsEntity>() {
            override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}