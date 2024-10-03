package com.example.feedflow

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.loader.content.AsyncTaskLoader

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Log.d("onCreate called")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private inner class DownloadData: AsyncTask<String, Void, String>(){
        private val TAG = "DownloadData"

    @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String?){
           super.onPostExecute(result)
        }

        override fun doInBackground(vararg params: String?): String {
            TODO("Not yet implemented")
        }
}