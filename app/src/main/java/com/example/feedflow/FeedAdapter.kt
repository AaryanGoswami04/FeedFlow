package com.example.feedflow

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.w3c.dom.Text

class FeedAdapter(context: Context, val resource: Int, private val applications: List<FeedEntry>) : ArrayAdapter<FeedEntry>(context, resource, applications){
    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from((context))

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(resource, parent, false)

        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
        val tvSummary: TextView = view.findViewById(R.id.tvSummary)


        val currentApp = applications[position]

        tvName.text = currentApp.name
        tvArtist.text = currentApp.artist
        tvSummary.text = currentApp.summary

        return view
    }

    override fun getCount(): Int {
        Log.d(TAG, "getCount() Called")
        return applications.size
    }
}