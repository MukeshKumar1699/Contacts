package com.example.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.PhoneDisplayItemBinding
import com.example.contacts.listeners.EmailItemClickListener

class EmailAdapter(
    var emailList: List<EmailDetails>,
    val itemClickListener: EmailItemClickListener
) : RecyclerView.Adapter<EmailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {

        val binding =
            PhoneDisplayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {

        val email = emailList[position]
        holder.setData(email, itemClickListener)
    }

    override fun getItemCount(): Int {
        return emailList.size
    }

}

class EmailViewHolder(val binding: PhoneDisplayItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(emailDetails: EmailDetails, itemClickListener: EmailItemClickListener) {
        binding.tvPhoneNumber.text = emailDetails.mailID
        binding.tvPhoneType.text = emailDetails.type

        binding.ivCall.apply {
            visibility = View.INVISIBLE
        }
        binding.ivMessage.apply {
        }.setOnClickListener {

            itemClickListener.onItemClicked(adapterPosition, emailDetails)
        }
        binding.ivVideoCall.apply {
            visibility = View.GONE
        }
    }


}
