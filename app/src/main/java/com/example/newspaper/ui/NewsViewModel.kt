package com.example.newspaper.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newspaper.data.NewsResponse
import com.example.newspaper.database.NewsRepository
import com.example.newspaper.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val repo : NewsRepository) : ViewModel()
{
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNewsPage = 1        // update later

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNewsPage = 1        // update later

    init {
        getBreakingNews()
    }

/**For BreakingNewsFragment**/
    fun getBreakingNews(countryString : String = "eg") = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = repo.getBreakingNews(countryString, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>
    {
        if(response.isSuccessful)
            response.body().let {
                return Resource.Success(it)
            }
        else
            return Resource.Error(response.message(), response.body())
    }

/**For SearchNewsFragment**/
    fun searchForNews(text : String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repo.getSearchedNews(text, searchNewsPage)
        searchNews.postValue(handleSearchForNewsResponse(response))
    }

    private fun handleSearchForNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>
    {
        if(response.isSuccessful)
            response.body().let {
                return Resource.Success(it)
            }
        else
            return Resource.Error(response.message(), response.body())
    }














    class NewsVMFactory(val repo: NewsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NewsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return NewsViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}