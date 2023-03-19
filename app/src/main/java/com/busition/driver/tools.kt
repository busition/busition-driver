package com.busition.driver

import android.view.View

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private val ARG_PARAM1 = "param1"
private val ARG_PARAM2 = "param2"

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

val prod_api_host = "api.busition.com"
val prod_api_url = "https://${prod_api_host}/"

val dev_api_host = "api.dev.busition.com"
val dev_api_url = "http://${dev_api_host}:8001/"
