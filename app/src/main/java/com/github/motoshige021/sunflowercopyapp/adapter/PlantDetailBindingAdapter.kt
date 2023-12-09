package com.github.motoshige021.sunflowercopyapp.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.DeadObjectException
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.github.motoshige021.sunflowercopyapp.utilties.TAG
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    // https://qiita.com/kaleidot725/items/b2ca56bfc5a8120efcea
    if (!imageUrl.isNullOrEmpty()) {
        view.load(imageUrl) {
            this.crossfade(true)
        }
    }
    /* サンプルがCoilライブラリを使用してたので差し替え-削除
    if (!imageUrl.isNullOrEmpty()) {
        runBlocking {
            setImageUri(view, imageUrl)
        }
    }
    */
}

suspend fun setImageUri(view: ImageView, imageUrl: String) {
    // https://akira-watson.com/android/httpurlconnection-get.html
    Executors.newSingleThreadExecutor().execute {
        // https://qiita.com/Ryuya_KusozkoImomusi/items/c1eec945fc08a159f840
        try {
            var connection = URL(imageUrl).openConnection()
            connection.doInput = true
            connection.connect()
            var input = connection.getInputStream()
            var bitmap = BitmapFactory.decodeStream(input)
            view.setImageBitmap(null)
            view.setImageBitmap(bitmap)
        } catch (ex: IOException) {
            ex.message?.let { Log.e(TAG, it) }
        } catch(ex: Exception) {
            ex.message?.let { Log.e(TAG, it) }
        }
    }
}