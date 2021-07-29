package com.example.tipranksapp.View

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tipranksapp.Model.Article
import com.example.tipranksapp.Model.DownloadImageTask
import com.example.tipranksapp.Model.imageHolder
import com.example.tipranksapp.R
import com.example.tipranksapp.ViewModel.AppViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel : AppViewModel
    private lateinit var listView : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        setupObservers()
        setupSearch()
        setupListView(listOf())
    }

    fun setupObservers(){
        mainActivityViewModel.mutableArticleList.observe(this, Observer {
            setupListView(it)
            imageHolder.authorImage = mutableListOf()
            imageHolder.thumbnailImage = mutableListOf()
            it.forEach{
                imageHolder.authorImage.add(null)
                imageHolder.thumbnailImage.add(null)
            }
        })
        mainActivityViewModel.mutableIsSearching.observe(this, Observer { isSearching ->
            if(isSearching){ loadingPanel.visibility = View.VISIBLE }
            else{ loadingPanel.visibility = View.INVISIBLE }
        })
    }

    fun setupSearch(){
        searchBar.setOnSearchClickListener {
            mainActivityViewModel.search(searchBar.query.toString())
        }
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainActivityViewModel.search(searchBar.query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        searchBar.setOnClickListener{
            searchBar.isIconified = false
        }
    }

    fun setupListView(articleList : List<Article>){
        listView = findViewById(R.id.listView)
        val adapter = ArticleAdapter(this, articleList)
        listView.adapter = adapter
        Log.d("dadadad",articleList.count().toString())
        listView.setOnItemClickListener { parent, view, position, id ->
            if(position % 10 == 3){
                val detailIntent = ArticleActivity.newIntent(this, "https://www.tipranks.com/")
                startActivity(detailIntent)
            }else {
                val detailIntent = ArticleActivity.newIntent(this, articleList[position].link)
                startActivity(detailIntent)
            }
        }
    }
}

class ArticleAdapter(private val context: Context,
                     private val dataSource: List<Article>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        var rowView = convertView
        val article = getItem(position) as Article
        if((position - position / 10) % 10 == 3){
            rowView = inflater.inflate(R.layout.promotion_item, parent, false)
        }else{
            rowView = inflater.inflate(R.layout.article_item, parent, false)
            var thumbnail = rowView.findViewById<ImageView>(R.id.thumbnailImageView)
            var authorImage = rowView.findViewById<ImageView>(R.id.authorImageView)
            var descriptionText = rowView.findViewById<TextView>(R.id.descriptionTextView)
            var headlineText = rowView.findViewById<TextView>(R.id.headlineTextView)
            var nameDateText = rowView.findViewById<TextView>(R.id.nameDateTextView)
            descriptionText.text = article.description
            headlineText.text = article.headline
            nameDateText.text = article.author

            if(imageHolder.authorImage[position] != null) {
                authorImage.setImageBitmap(imageHolder.authorImage[position]!!)
            }else{
                DownloadImageTask(authorImage) {
                    imageHolder.authorImage.add(position, it)
                }.execute(article.authorImageURL)!!
            }
            if(imageHolder.thumbnailImage[position] != null) {
                thumbnail.setImageBitmap(imageHolder.thumbnailImage[position]!!)
            }else {
                DownloadImageTask(thumbnail) {
                    imageHolder.thumbnailImage.add(position, it)
                }.execute(article.thumbnailURL)!!
            }
        }
        return rowView!!
    }
}


