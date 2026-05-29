package com.example.k2offlineonlinenewsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.k2offlineonlinenewsapp.data.NewsRepository
import com.example.k2offlineonlinenewsapp.data.local.NewsEntity
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    val newsList: LiveData<List<NewsEntity>> = repository.getNews()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _currentCategory = MutableLiveData<String>("business")
    val currentCategory: LiveData<String> = _currentCategory

    init {
        refreshNews()
    }

    fun setCategory(category: String) {
        if (_currentCategory.value != category) {
            _currentCategory.value = category
            refreshNews()
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val category = _currentCategory.value ?: "business"
                repository.refreshNews(category)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown Error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    class Factory(private val repository: NewsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}