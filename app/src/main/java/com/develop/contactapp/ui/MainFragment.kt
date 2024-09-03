package com.develop.contactapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.develop.contactapp.ContactApp
import com.develop.contactapp.R
import com.develop.contactapp.databinding.FragmentMainBinding
import com.develop.contactapp.model.ContactAdapter
import com.develop.contactapp.model.User
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject

class MainFragment : Fragment() {
    lateinit var mainFragBinging : FragmentMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactAdapter
    val userList : MutableList<User> = mutableListOf()

    private lateinit var  appContext : Context

    private lateinit var appInstance : ContactApp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainFragBinging = FragmentMainBinding.inflate(inflater, container, false)

        recyclerView = mainFragBinging.contactRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(mainFragBinging.root.context)

        appContext = mainFragBinging.root.context.applicationContext
        appInstance = ContactApp.app

        return mainFragBinging.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ContactAdapter(
            userList,
            onEditButtonClick = { user : User->
                val query = appInstance.collectionReference
                    .whereEqualTo("phoneNumber",user.phoneNumber)
                query.get().addOnSuccessListener { docs ->
                    if(!docs.isEmpty){
                        var docId = ""
                        for(doc in docs){
                            docId = doc.id
                        }
                        if(docId.isNotBlank() && docId.isNotEmpty()){
                            val bundle = Bundle().apply {
                                putParcelable("userdata",user)
                                putBoolean("isEdit",true)
                                putString("docid",docId)
                            }
                            Navigation
                                .findNavController(view)
                                .navigate(R.id.action_mainFragment_to_addUserFragment,bundle)
                        }

                    }else{
                        Toast.makeText(appContext, "Can't edit this entry", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            },
            onDeleteButtonClick = { phoneNo,adapterPosition->
                val query = appInstance.collectionReference
                    .whereEqualTo("phoneNumber",phoneNo)
                query.get().addOnSuccessListener { docs ->
                    if(!docs.isEmpty){
                        var docId = ""
                        for(doc in docs){
                            docId = doc.id
                        }
                        if(docId.isNotBlank() && docId.isNotEmpty()){
                            val docRef = appInstance.fbDb.collection("Users").document(docId)
                            docRef.delete().addOnSuccessListener {
                                Toast.makeText(appContext, "Deleted Successfully..", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            userList.removeAt(adapterPosition)
                            adapter.notifyItemRemoved(adapterPosition)
                            adapter.notifyItemRangeChanged(adapterPosition, userList.size)
                        }

                    }else{
                        Toast.makeText(appContext, "Can't edit this entry", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        )
        recyclerView.adapter = adapter

        mainFragBinging.fabAddUser.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addUserFragment)
        }

        retrieveDataFromFireStore()
        Log.d(ContactApp.LIFECYCLE,"MainFragment ViewCreated")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveDataFromFireStore() {
        appInstance.collectionReference.get().addOnSuccessListener{ querySnapShot : QuerySnapshot ->
            userList.clear()
            for(snapshot : QueryDocumentSnapshot in querySnapShot){
                val userData = snapshot.toObject<User>()
                userList.add(userData)
            }
            adapter.notifyDataSetChanged()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(ContactApp.LIFECYCLE,"MainFragment View Destroyed")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(ContactApp.LIFECYCLE,"MainFragment Destroyed")
    }

}