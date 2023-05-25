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

fun appendString(a: String, b: String, c: String = ""): String {
    return buildString {
        append(a)
        append(b)
        append(c)
    }
}

val prod_api_protocol = "https"
val prod_api_host = "api.busition.com"
val prod_api_url = "${prod_api_protocol}://${prod_api_host}/"

val dev_api_host = "api.dev.busition.com"
val dev_api_protocol = "http"
val dev_api_port = 8001
val dev_api_version = "latest"
val dev_api_url = "${dev_api_protocol}://${dev_api_host}:${dev_api_port}/${dev_api_version}/"

val base_api_url = dev_api_url
