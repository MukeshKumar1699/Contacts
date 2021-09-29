@file:Suppress("RedundantExplicitType")

package com.example.contacts

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.ContactsItemBinding
import com.example.contacts.listeners.ContactsItemClickListener


class ContactsAdapter(
    var contactList: ArrayList<Contacts>,
    val itemClickListener: ContactsItemClickListener
) :
    RecyclerView.Adapter<ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {

        val contactsItemBinding =
            ContactsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(contactsItemBinding)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {

        val contact = contactList[position]
        holder.setData(contact, itemClickListener)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(contactList: ArrayList<Contacts>) {

//        this.contactList.clear()
        this.contactList = contactList
        notifyDataSetChanged()
    }
}

class ContactsViewHolder(val binding: ContactsItemBinding) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var itemClickListener: ContactsItemClickListener

    fun setData(contact: Contacts, itemClickListener: ContactsItemClickListener) {

        binding.tvName.text = contact.name
        this.itemClickListener = itemClickListener

        if (!contact.image.equals("null")) {
            binding.sivContactImage.setImageURI(Uri.parse(contact.image))

        } else {
            binding.tvContactInitial.text = contact.name.get(0).toString()
            binding.sivContactImage.setBackgroundColor(contact.color.toInt())
        }

        binding.cvItem.setOnClickListener {
            itemClickListener.onItemClicked(position = adapterPosition, contacts = contact)
        }

    }

    companion object {
        private const val TAG = "ContactsAdapter"
    }

}
