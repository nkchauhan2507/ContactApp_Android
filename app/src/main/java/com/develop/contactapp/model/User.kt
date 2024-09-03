package com.develop.contactapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username : String?  = null,
    val phoneNumber : String? = null,
    val userGroup : String? = null
) : Parcelable {

    constructor() : this("","","")

}
