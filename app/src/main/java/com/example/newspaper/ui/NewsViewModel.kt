package com.example.newspaper.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.net.TransportInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.example.newspaper.data.Article
import com.example.newspaper.data.NewsResponse
import com.example.newspaper.database.NewsRepository
import com.example.newspaper.utils.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(app: Application, private val repo : NewsRepository) : AndroidViewModel(app)
{
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var currentBreakingNewsResponse : NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var currentSearchNewsResponse : NewsResponse? = null

    val savedNews : LiveData<List<Article>?> by lazy {
        repo.getSavedNews()
    }

//    var addedBefore = false
    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getBreakingNews()
    }


    /**For BreakingNewsFragment**/
    fun getBreakingNews(countryString : String = "eg") = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        breakingNewsSafeCall(countryString)
    }

    private suspend fun breakingNewsSafeCall(countryString : String = "eg") {
        try {
            if (hasInternetConnection()){
                val response = repo.getBreakingNews(countryString, breakingNewsPage)
                // handle this response first and check its result, before posting it
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }
            else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }
        }
        catch (t: Throwable) {
            when(t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>
    {
        if(response.isSuccessful)
            response.body().let {
                breakingNewsPage++

                if(currentBreakingNewsResponse == null)
                    currentBreakingNewsResponse = it
                else{
                    currentBreakingNewsResponse?.articles?.addAll(it!!.articles)    //incrementing the list of news
                }

                return Resource.Success(currentBreakingNewsResponse)
            }
        else
            return Resource.Error(response.message(), response.body())
    }

    /**For SearchNewsFragment**/
    fun searchForNews(text : String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        searchNewsSafeCall(text)
    }

    private suspend fun searchNewsSafeCall(text : String) {
        try {
            if (hasInternetConnection()){
                val response = repo.getSearchedNews(text, searchNewsPage)
                // handle this response first and check its result, before posting it
                searchNews.postValue(handleSearchForNewsResponse(response))
            }
            else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        }
        catch (t: Throwable) {
            when(t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleSearchForNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>
    {
        if(response.isSuccessful)
            response.body().let {
                it?.articles?.size //////////////////////////////////////////////////////////
                searchNewsPage++

                if(currentSearchNewsResponse == null)
                    currentSearchNewsResponse = it
                else{
                    currentSearchNewsResponse?.articles?.addAll(it!!.articles)    //incrementing the list of news
                }

                return Resource.Success(currentSearchNewsResponse)
            }
        else
            return Resource.Error(response.message(), response.body())
    }

    /**Deal with Room**/
    fun saveThisArticle(article: Article) =
        viewModelScope.launch {
//            val f1 = savedNews.value?.size
            repo.addIfNotExist(article)
            invalidateShowNoDataForSavedNews()
//            val f2 = savedNews.value?.size
//            addedBefore = f1 == f2
    }

    fun deleteThisArticle(article: Article) = viewModelScope.launch {
        repo.deleteArticle(article)
        invalidateShowNoDataForSavedNews()
    }

    fun invalidateShowNoDataForSavedNews() {
        showNoData.value = false

//        showNoData.value = savedNews.value!!.isNullOrEmpty()
    }

    /**Handle internet connection**/
    private fun hasInternetConnection() : Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }



    class NewsVMFactory(val app: Application, val repo: NewsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NewsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return NewsViewModel(app, repo) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}