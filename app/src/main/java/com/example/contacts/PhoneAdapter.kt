package com.example.contacts

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.PhoneDisplayItemBinding

class PhoneAdapter(var phoneList: List<PhoneDetails>) : RecyclerView.Adapter<PhoneViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {

        val binding = PhoneDisplayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {

        val phone = phoneList[position]
        holder.setData(phone)    }

    override fun getItemCount(): Int {
        return phoneList.size
    }

}

class PhoneViewHolder(val binding: PhoneDisplayItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(phone: PhoneDetails) {
        binding.tvPhoneNumber.text = phone.number
        binding.tvPhoneType.text = phone.label
    }


}
