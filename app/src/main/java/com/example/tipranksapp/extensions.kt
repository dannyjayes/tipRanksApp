package com.example.tipranksapp

import android.util.Log
import com.example.tipranksapp.Model.Article
import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it])
    {
        is JSONArray ->
        {
            val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else            -> value
    }
}

fun List<Map<String,*>>?.toArticleList(): List<Article>{
    if(this == null){return listOf()}
    var articleList = mutableListOf<Article>()
    if(this == null){return listOf()}
    var position = 0
    this.forEach {articleMap ->
        val headline = articleMap["title"].toString()
        var description = articleMap["description"].toString()
        description = description.removeRange(0..2)
        val date = articleMap["date"].toString()
        val authorMap = articleMap["author"] as Map<String, *>
        val authorImageURLMap = articleMap["image"] as Map<String, *>
        val thumbnailURLMap = articleMap["thumbnail"] as Map<String, *>
        val author = authorMap["name"].toString()
        var thumbnailURL = thumbnailURLMap["src"].toString()
        val authorImageURL = authorImageURLMap["src"].toString()
        val link = articleMap["link"].toString()

        articleList.add(Article(author, headline, description,date, link, authorImageURL, thumbnailURL))
        if(position %10 == 3){
            articleList.add(Article(author, headline, description,date, link, authorImageURL, thumbnailURL))
            position +=1
        }
        position +=1
    }
    return articleList
}