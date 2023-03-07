package com.busition.driver.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    var displayName: String,
    var driver_id : Int
    //... other data fields that may be accessible to the UI
)