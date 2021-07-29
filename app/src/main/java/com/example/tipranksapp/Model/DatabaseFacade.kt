package com.example.tipranksapp.Model

import android.os.AsyncTask
import com.example.tipranksapp.toArticleList
import com.example.tipranksapp.toMap
import org.json.JSONObject
import java.net.URL

object Database{
    private var cachedArticlesList : List<Article>? = null
    private var cachedQuery : String? = null
    fun getArticles(query : String, onComplete: (List<Article>) -> Unit){
        if(cachedQuery != null && cachedArticlesList != null){
            if(cachedQuery == query){
                onComplete(cachedArticlesList!!)
            }
        }
        FetchData(query) { articleList ->
            onComplete(articleList.toArticleList())
            if(articleList != null){cachedArticlesList = articleList.toArticleList()}
        }.execute()
    }
}

private class FetchData(val query : String, val onComplete : (List<Map<String, *>>?) -> Unit) : AsyncTask<Void, Void, Void>() {
    var queryResult : String? = null

    override fun doInBackground(vararg params: Void?): Void? {
        queryResult = null
        queryResult = URL("https://www.tipranks.com/api/news/posts?page=1&per_page=20&search=$query").readText()
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        val jsonObject = JSONObject(queryResult)
        val map = jsonObject.toMap() //Gets me a map with "data", which contains a list of maps.
        var dataList = map["data"] as List<Map<String, *>>
        onComplete(dataList)
    }
}
