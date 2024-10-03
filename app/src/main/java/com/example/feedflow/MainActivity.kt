package com.example.feedflow

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.loader.content.AsyncTaskLoader
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates

class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""

    override fun toString(): String {
        return """ 
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            imageURL = $imageURL
        """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val xmlListView: ListView = findViewById(R.id.xmlListView)

        Log.d(TAG, "onCreate called")
        val downloadData = DownloadData(this, xmlListView)
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topalbums/limit=10/xml")
        Log.d(TAG, "onCreate: done")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
/* String (First Parameter) — Input Parameter Type
This represents the type of data that is passed to the background task when you start the AsyncTask.*/
    companion object {
        private class DownloadData(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"
/**/
            var propContext: Context by Delegates.notNull()
            var propListView: ListView by Delegates.notNull()

            init{
                propContext  =context
                propListView = listView
            }

            @Deprecated("Deprecated in Java")
            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                val parseApplications = ParseApplications()
                if (result != null) {
                    parseApplications.parse(result)
                }

                val arrayAdapter = ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplications.applications)
                propListView.adapter = arrayAdapter
            }

            //handles the task of downloading data from the URL.
            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground: Starts with ${url[0]}")
                val rssFeed =
                    downloadURL(url[0]) //calls the downloadURL() function, passing the URL, to actually download the content.
                if (rssFeed.isEmpty()) {  //If the  rssFeed is empty(indicating a failure), it logs an error message.
                    Log.e(TAG, "doInBackground: Error in downloading")
                }
                return rssFeed //returns the content
            }

            //Responsible for establishing a connection to the provided URL and downloading its contents
            private fun downloadURL(urlPath: String?): String {
                val xmlResult = StringBuilder() //to store the downloaded data

                try {
                    val url =
                        URL(urlPath) //A URL object is created from the string urlPath, and then an HttpURLConnection is opened to that URL.
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    val response = connection.responseCode
                    Log.d(
                        TAG,
                        "downloadXML: The response code was $response"
                    ) //The HTTP response code is retrieved and logged

                    //used for reading data from an input stream (typically from a network connection) and accumulating that data into xmlResult
                    //BufferedReader object, which is used to read text from a character-input stream.
                    //InputStreamReader(connection.inputStream) converts the InputStream from the HTTP connection into a character stream, allowing you to read it as text.
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))

                    val inputBuffer =
                        CharArray(500) //Temporarily stores characters read from the input stream
                    var charsRead = 0
                    while (charsRead >= 0) { //Until no more characters are there to read

                        //reads up to 500 characters from the stream into inputBuffer
                        //returns the actual number of characters read, which is stored in charsRead
                        charsRead = reader.read(inputBuffer)

                        //indicating that some characters were read), the code converts the inputBuffer (from index 0 to charsRead) into a String,
                        //appends it to xmlResult.
                        if (charsRead > 0) {
                            xmlResult.append(String(inputBuffer, 0, charsRead))
                        }
                    }
                    reader.close()



                    Log.d(TAG, "Received ${xmlResult.length} bytes")
                    return xmlResult.toString()

                } catch (e: Exception) {
                    val errorMessage: String = when (e) {
                        is MalformedURLException -> "downloadXML: Invalid URL ${e.message}" //If there's a problem with the URL format
                        is IOException -> "downloadXML: IO Exception reading data: ${e.message}"   //If there's an I/O issue (e.g., network error)
                        is SecurityException -> ":downloadURL: Security exception. Needs Permission ${e.message}"
                        else -> "Unknown error: ${e.message}" //Any other generic exception is caught and logged
                    }
                }
                return "" //If control reaches here, there was a problem, so return empty string
            }
        }
    }
}