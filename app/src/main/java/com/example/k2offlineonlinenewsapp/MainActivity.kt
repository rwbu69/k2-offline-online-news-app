package com.example.k2offlineonlinenewsapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.k2offlineonlinenewsapp.databinding.ActivityMainBinding
import com.example.k2offlineonlinenewsapp.di.Injection

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NewsViewModel by viewModels {
        NewsViewModel.Factory(Injection.provideRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter
        }

        viewModel.newsList.observe(this) { news ->
            newsAdapter.submitList(news)
            binding.tvNewsCount.text = "${news.size} Berita"
            binding.emptyState.visibility = if (news.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.errorBanner.visibility = View.VISIBLE
                binding.tvErrorMessage.text = "Gagal memuat berita: $errorMessage"
                Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            } else {
                binding.errorBanner.visibility = View.GONE
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshNews()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.refreshNews()
        }
    }
}
