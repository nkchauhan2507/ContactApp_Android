package com.develop.contactapp

import android.app.Application
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class ContactApp : Application() {

    lateinit var fbDb : FirebaseFirestore
//    lateinit var contactRef : DocumentReference

    lateinit var  collectionReference : CollectionReference
    companion object {
        lateinit var app : ContactApp
            private set

        const val LIFECYCLE = "lifecycle"
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        fbDb = FirebaseFirestore.getInstance()
//        contactRef = fbDb.collection("Contact").document("Users")
        collectionReference = fbDb.collection("Users")

    }
}