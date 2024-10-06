package com.example.feedflow

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
/* The ParseApplications class is responsible for parsing XML data received from an RSS feed,
extracting relevant information about feed entries*/
class ParseApplications {
    private val TAG = "ParseApplications"
    val applications = ArrayList<FeedEntry>() // An ArrayList that holds FeedEntry objects. Each FeedEntry represents a single item from the RSS feed (e.g., an application, article, etc.).

    //parse function takes an XML string (xmlData) as input and parses it to extract feed entries, populating the applications list with FeedEntry objects
    //returns a Boolean indicating whether the parsing was successful
    fun parse(xmlData: String): Boolean{
      //  Log.d(TAG, "parse called with $xmlData")
        var status = true //Indicates the success or failure of the parsing process.
        var inEntry = false //A flag to determine if the parser is currently within an <entry> tag.
        var textValue = ""  // Temporarily holds the text content between XML tags.

        try{
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())

            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            while(eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase()

                when (eventType) {

                    XmlPullParser.START_TAG -> {
                  //      Log.d(TAG, "Parse: starting tag for: " + tagName)
                        //parsing of a new entry has begun.
                        if (tagName == "entry") {
                            inEntry = true;
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xpp.text //Captures the text content between tags and stores it in textValue.

                    XmlPullParser.END_TAG -> {
                       // Log.d(TAG, "Parse: Ending tag for: " + tagName)
                        /* If currently inside an <entry>, it checks the tag name and assigns textValue to the corresponding property of currentRecord.*/

                        if (inEntry) {
                            when (tagName) {
                                /*adds the completed currentRecord to the applications list.
                                resets inEntry to false, and initializes a new FeedEntry for the next entry.*/
                                "entry" -> {
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry()
                                }

                                "name"->currentRecord.name = textValue
                                "artist"->currentRecord.artist = textValue
                                "releasedate"->currentRecord.releaseDate = textValue
                                "summary"->currentRecord.summary = textValue
                                "image"->currentRecord.imageURL = textValue
                            }
                        }
                    }
                }
                //Nothing else to do
                eventType = xpp.next()
            }
            for(app in applications){
                Log.d(TAG,"******************")
                Log.d(TAG, app.toString())
            }
        }catch(e: Exception){
            e.printStackTrace()
            status = false
        }
        return status
    }
}