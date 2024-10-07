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
    val tvSummary: TextView = v.findViewById(R.id.tvSummary)
}
class FeedAdapter(context: Context, val resource: Int, private val applications: List<FeedEntry>) : ArrayAdapter<FeedEntry>(context, resource, applications){
  //  private val TAG = "FeedAdapter"
    // obtains the LayoutInflater instance associated with the given context.
    //LayoutInflater: it reads an XML file describing a layout and converts it into actual UI elements that can be displayed on the screen.
    private val inflater = LayoutInflater.from((context))

    //The getView method is called by the ListView to obtain the view for each item in the data set
    //position: The position of the item within the adapter's data set.
    //convertView: The old view to reuse, if possible.
    //parent: The parent that this view will eventually be attached to.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        /*The ViewHolder pattern is a design pattern used to improve the performance of list-based UI components like ListView or RecyclerView.
        Its primary purpose is to minimize the number of calls to findViewById, which can be resource-intensive, especially in large lists. */

        if(convertView == null){ //No existing view to reuse, so new view must be created
//            Log.d(TAG, "getView Called with NULL convertView")
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{   //Reuse the Existing View:
//            Log.d(TAG, "getView Called with a convertView")
            view = convertView
            viewHolder = view.tag as ViewHolder // Retrieving the ViewHolder from the view's tag
        }


        //// Get the current data item
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