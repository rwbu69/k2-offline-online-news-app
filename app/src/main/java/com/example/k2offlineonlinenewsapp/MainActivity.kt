package com.example.k2offlineonlinenewsapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.k2offlineonlinenewsapp.databinding.ActivityMainBinding
import com.example.k2offlineonlinenewsapp.di.Injection
import com.example.k2offlineonlinenewsapp.ui.CategoryAdapter
import com.example.k2offlineonlinenewsapp.ui.NewsAdapter
import com.example.k2offlineonlinenewsapp.ui.NewsViewModel
import com.example.k2offlineonlinenewsapp.ui.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter

    private lateinit var categoryAdapter: CategoryAdapter

    private val categories = listOf(
        "business",
        "entertainment",
        "general",
        "health",
        "science",
        "sports",
        "technology"
    )

    private val newsViewModel: NewsViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    fun setupUI() {
        binding.swipeRefresh.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorAccent
        )

        binding.swipeRefresh.setOnRefreshListener {
            newsViewModel.refreshNews()
        }

        binding.btnRetry.setOnClickListener {
            binding.errorBanner.visibility = View.GONE
            newsViewModel.refreshNews()
        }
    }

    fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvNews.adapter = newsAdapter
        binding.rvNews.setHasFixedSize(true)

        binding.rvNews.itemAnimator?.apply {
            addDuration = 300
            changeDuration = 200
        }

        categoryAdapter = CategoryAdapter(
            categories = categories,
            selectedCategory = newsViewModel.currentCategory.value ?: "business"
        ) { category ->
            newsViewModel.setCategory(category)
        }
        binding.rvCategories.apply {
            adapter = categoryAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    fun observeViewModel() {
        newsViewModel.newsList.observe(this) { newsList ->
            newsAdapter.submitList(newsList)

            val isEmpty = newsList.isNullOrEmpty()
            binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.rvNews.visibility = if (isEmpty) View.GONE else View.VISIBLE

            if (!isEmpty) {
                binding.tvNewsCount.text = "${newsList.size} Berita"
                binding.tvNewsCount.visibility = View.VISIBLE
            } else {
                binding.tvNewsCount.visibility = View.GONE
            }
        }

        newsViewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }

        newsViewModel.error.observe(this) { message ->
            if (message != null) {
                binding.errorBanner.visibility = View.VISIBLE
                binding.tvErrorMessage.text = "Gagal Load : ${message}"
            } else {
                binding.errorBanner.visibility = View.GONE
            }
        }

        newsViewModel.currentCategory.observe(this) { category ->
            categoryAdapter.updateSelectedCategory(category)

        }
    }
}
