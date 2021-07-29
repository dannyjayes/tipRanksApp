package com.example.tipranksapp.ViewModel

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipranksapp.Model.Article
import com.example.tipranksapp.Model.Database

class AppViewModel : ViewModel() {
    var mutableArticleList : MutableLiveData<List<Article>> = MutableLiveData(listOf())
    var mutableIsSearching : MutableLiveData<Boolean> = MutableLiveData(false)

    fun init(){
    }

    fun search(query : String){
        mutableIsSearching.postValue(true)
        Database.getArticles(query) {articleList ->
            Log.d("value before", mutableArticleList.value!!.count().toString())
            mutableArticleList.postValue(articleList)
            mutableIsSearching.postValue(false)
            Log.d("value after", mutableArticleList.value!!.count().toString())
            Log.d("dalkhjdlkahjflahfl", "whoooohoooo")
        }
    }

}

