@file:Suppress("RedundantExplicitType")

package com.example.contacts

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.ContactsItemBinding
import java.util.*


class ContactsAdapter(
    var contactList: ArrayList<Contacts>,
    val itemClickListener: ContactsItemClickListener
) :
    RecyclerView.Adapter<ContactsViewHolder>() {

    private val TAG = "ContactsAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")


        val contactsItemBinding =
            ContactsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(contactsItemBinding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")

        val contact = contactList[position]
        holder.setData(contact, itemClickListener)
    }

    override fun onViewRecycled(holder: ContactsViewHolder) {
        super.onViewRecycled(holder)
        Log.d(TAG, "onViewRecycled: ")
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${contactList.size} ")

        return contactList.size
    }

    fun deleteItem(position: Int) {
//        mRecentlyDeletedItem = contactList.get(position)
//        mRecentlyDeletedItemPosition = position

        contactList.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(contactList: ArrayList<Contacts>) {
        Log.d(TAG, "updateData: ")

        this.contactList.clear()
        this.contactList = contactList
        notifyDataSetChanged()
    }
}

class ContactsViewHolder(val binding: ContactsItemBinding) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var itemClickListener: ContactsItemClickListener

    fun setData(contact: Contacts, itemClickListener: ContactsItemClickListener) {

        binding.tvName.text = contact.name
        this.itemClickListener = itemClickListener

//        if (Integer.parseInt(contact.name.trim()[0].toString()) in 65..90 &&
//            Integer.parseInt(contact.name.trim()[0].toString()) in 97..122
//        ) {
//            binding.tvContactInitial.text = contact.name[0].toString()
//        } else {
//            binding.sivContactImage.setImageResource(R.drawable.ic_baseline_person_24)
//        }

//        val r: Random = Random()
//        val red: Int = r.nextInt(255 - 0 + 1) + 0
//        val green: Int = r.nextInt(255 - 0 + 1) + 0
//        val blue: Int = r.nextInt(255 - 0 + 1) + 0
//
//        val draw = GradientDrawable()
//        draw.shape = GradientDrawable.RECTANGLE
//        draw.setColor(Color.rgb(red, green, blue))
//        binding.sivContactImage.setBackground(draw)

        binding.cvItem.setOnClickListener {
            itemClickListener.onItemClicked(position = adapterPosition, contacts = contact)
        }

    }




}
