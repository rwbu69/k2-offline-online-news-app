package com.example.k2offlineonlinenewsapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.k2offlineonlinenewsapp.databinding.ItemCategoryBinding
import java.util.Locale

class CategoryAdapter(
    private val categories: List<String>,
    private var selectedCategory: String,
    private val onCategoryClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            val displayName = category.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }

            binding.tvCategoryName.text = displayName

            val isSelected = category == selectedCategory
            binding.tvCategoryName.isSelected = isSelected

            binding.tvCategoryName.setOnClickListener {
                if (category != selectedCategory) {
                    onCategoryClick(category)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun updateSelectedCategory(category: String) {
        if (category != selectedCategory) {
            val oldSelected = selectedCategory
            selectedCategory = category

            val oldIndex = categories.indexOf(oldSelected)
            val newIndex = categories.indexOf(category)
            if (oldIndex != -1) notifyItemChanged(oldIndex)
            if (newIndex != -1) notifyItemChanged(newIndex)
        }
    }
}
