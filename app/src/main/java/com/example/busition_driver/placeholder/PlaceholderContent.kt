package com.example.busition_driver.placeholder

import com.example.busition_driver.StationDetailFragment
import java.util.ArrayList
import java.util.HashMap


object PlaceholderContent {


    val ITEMS: MutableList<PlaceholderItem> = ArrayList()
    val ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()

    private val COUNT = 3

    init {
        var id = StationDetailFragment.routeId
        for (i in 1..COUNT) {
            addItem(createPlaceholderItem(i,id))
        }
    }

    private fun addItem(item: PlaceholderItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createPlaceholderItem(position: Int, id:String): PlaceholderItem {
        if(position == 3){
            return PlaceholderItem(id, "Item " + position, makeDetails(position), "test")
        }
        return PlaceholderItem(position.toString(), "Item " + position, makeDetails(position), "")
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A placeholder item representing a piece of content.
     */
    data class PlaceholderItem(val id: String, val content: String, val details: String, val type: String){
        override fun toString(): String = content
    }
}