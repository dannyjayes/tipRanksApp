package com.example.tipranksapp.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.tipranksapp.Model.Article
import com.example.tipranksapp.R

class ArticleActivity : AppCompatActivity() {
    private lateinit var webView : WebView
    companion object {

        fun newIntent(context: Context, articleURL: String): Intent {
            val detailIntent = Intent(context, ArticleActivity::class.java)

            detailIntent.putExtra("url", articleURL)

            return detailIntent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        val url = intent.extras!!.getString("url")
        setTitle(title)
        webView = findViewById(R.id.webView)
        webView.loadUrl(url)
    }
}