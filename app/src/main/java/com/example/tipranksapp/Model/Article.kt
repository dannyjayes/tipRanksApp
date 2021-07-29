package com.example.tipranksapp.Model

import android.graphics.Bitmap

data class Article(val author : String, val headline : String, val description : String, val date : String, val link : String, var authorImageURL : String, var thumbnailURL : String) {
}