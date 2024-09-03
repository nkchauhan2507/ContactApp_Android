package com.develop.contactapp.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.develop.contactapp.R
import com.develop.contactapp.databinding.ItemCardBinding

class ContactAdapter(
    private val contactList : List<User>,
    private val onEditButtonClick : (user: User) -> Unit,
    private val onDeleteButtonClick : (phoneNo : String,pos : Int) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(val itemCardBinding : ItemCardBinding) : RecyclerView.ViewHolder(itemCardBinding.root) {
        fun bind(userData : User){
            itemCardBinding.user = userData
            itemCardBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_card,
            parent,
            false
        )
        binding.btnEdit.setIconResource(R.drawable.ic_edit)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currUserData = contactList[position]
        holder.bind(currUserData)
        holder.itemCardBinding.btnEdit.setOnClickListener {
            onEditButtonClick(contactList[position])
        }

        holder.itemCardBinding.btnDelete.setOnClickListener{
            onDeleteButtonClick(contactList[position].phoneNumber.toString(),position)
        }
    }

}