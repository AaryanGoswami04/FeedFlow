package com.example.feedflow

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.w3c.dom.Text

class ViewHolder(v: View){
    val tvName: TextView = v.findViewById(R.id.tvName)
    val tvArtist: TextView = v.findViewById(R.id.tvArtist)
    val tvSummary: TextView = v.findViewById(R.id.tvName)
}
class FeedAdapter(context: Context, val resource: Int, private val applications: List<FeedEntry>) : ArrayAdapter<FeedEntry>(context, resource, applications){
  //  private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from((context))

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if(convertView == null){
//            Log.d(TAG, "getView Called with NULL convertView")
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
//            Log.d(TAG, "getView Called with a convertView")
            view = convertView
            viewHolder = view.tag as ViewHolder
        }



        val currentApp = applications[position]

        viewHolder.tvName.text = currentApp.name
        viewHolder.tvArtist.text = currentApp.artist
        viewHolder.tvSummary.text = currentApp.summary

        return view
    }

    override fun getCount(): Int {
        //Log.d(TAG, "getCount() Called")
        return applications.size
    }
}