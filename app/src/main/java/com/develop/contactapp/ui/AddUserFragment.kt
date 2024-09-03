package com.develop.contactapp.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.develop.contactapp.ContactApp
import com.develop.contactapp.R
import com.develop.contactapp.databinding.FragmentAddUserBinding
import com.develop.contactapp.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.gson.Gson

class AddUserFragment : Fragment() {

    lateinit var appInstance : ContactApp
    lateinit var fragBinding: FragmentAddUserBinding

    private var isEdit : Boolean? = false
    private var user : User? = null
    private var documentId : String = ""
    private val gson = Gson()

    /***
     * Remove Fragment from backStack Task is Pending
     * */

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragBinding = FragmentAddUserBinding.inflate(inflater,container,false)
        appInstance = ContactApp.app

        user = arguments?.getParcelable("userdata",User::class.java)
        isEdit = arguments?.getBoolean("isEdit")
        documentId = arguments?.getString("docid").toString()

        if(isEdit == true) {
            fragBinding.btnSave.text = "Update"
        }else{
            fragBinding.btnSave.text = "Save"
        }

        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragBinding.btnSave.setOnClickListener {
            if(isEdit == true) {
                updateUserDataToFireStoreDB(view)
            } else {
                addUserToFireStoreDB(view)
            }
        }
    }

    private fun updateUserDataToFireStoreDB(view: View) {
        val updatedData = /*gson.toJson(*/
                User(
                fragBinding.etName.text.toString(),
                fragBinding.etPhoneNumber.text.toString(),
                fragBinding.etUserGroup.text.toString()
            )
//        )
        val docRef = appInstance.fbDb.collection("Users").document(documentId)

        docRef.get().addOnSuccessListener {docs ->
            if(docs.exists()){
                docRef.update(
                    "username",updatedData.username,
                    "phoneNumber",updatedData.phoneNumber,
                    "userGroup",updatedData.userGroup
                ).addOnSuccessListener {
                    navigateToMainFragment(view)
                    Toast.makeText(context?.applicationContext, "Updated Successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context?.applicationContext, "Update Failed...", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context?.applicationContext, "Not Exists..", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e->
            Toast.makeText(context?.applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMainFragment(view: View) {
        val navController = findNavController()
//        navController.popBackStack(R.id.mainFragment,true)
        navController.navigate(R.id.action_addUserFragment_to_mainFragment)
    }

    override fun onResume() {
        super.onResume()
        preFetchDataToUpdate(user)
    }

    private fun preFetchDataToUpdate(user: User?) {
        fragBinding.userData = user
    }

    private fun addUserToFireStoreDB(view: View) {
        val contactUser = User(
            fragBinding.etName.text.toString(),
            fragBinding.etPhoneNumber.text.toString(),
            fragBinding.etUserGroup.text.toString()
        )
        appInstance.collectionReference.add(contactUser).addOnSuccessListener { docRef : DocumentReference ->
            val cont = fragBinding.root.context.applicationContext
            Toast.makeText(cont, "Data Stored Successfully", Toast.LENGTH_SHORT).show()

            navigateToMainFragment(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(ContactApp.LIFECYCLE,"AddUserFragment View Destroyed")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(ContactApp.LIFECYCLE,"AddUserFragment Destroyed")
    }

}