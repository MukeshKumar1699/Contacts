package com.example.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.contacts.databinding.FragmentAddContactBinding
import com.google.android.material.textfield.TextInputLayout


class AddContactFragment : Fragment() {

    lateinit var binding: FragmentAddContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddContactBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPhone.setOnClickListener {

            val addPhoneView: View = layoutInflater.inflate(R.layout.field, null, false)

            val remove = addPhoneView.findViewById<ImageView>(R.id.close)
            val phoneTip = addPhoneView.findViewById<TextInputLayout>(R.id.tip_ETfield)

            phoneTip.hint = "Phone"

            binding.llCustomPhone.addView(addPhoneView)

            remove.setOnClickListener {
                binding.llCustomPhone.removeView(addPhoneView)
            }
        }

        binding.btnAddEmail.setOnClickListener {

            val addEmailView: View = layoutInflater.inflate(R.layout.field, null, false)

            val remove = addEmailView.findViewById<ImageView>(R.id.close)
            val phoneTip = addEmailView.findViewById<TextInputLayout>(R.id.tip_ETfield)

            phoneTip.hint = "Email"

            binding.llCustomEmail.addView(addEmailView)

            remove.setOnClickListener {
                binding.llCustomEmail.removeView(addEmailView)
            }
        }

        binding.ivDropDown.setOnClickListener {

            if (nameOptionIsClicked) {
                nameOptionIsClicked = false
                namesOptionVIsibility(View.GONE)
                binding.ivDropDown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)

            } else {
                nameOptionIsClicked = true
                namesOptionVIsibility(View.VISIBLE)
                binding.ivDropDown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }


        }

        binding.tvMoreFields.setOnClickListener {

            binding.tvMoreFields.visibility = View.GONE
            val addMoreView: View = layoutInflater.inflate(R.layout.more_fields, null, false)

            binding.llCustomPhone.addView(addMoreView)

        }

        binding.tvSave.setOnClickListener {


        }

    }


    fun namesOptionVIsibility(status: Int) {

        binding.tipNamePrefix.visibility = status
        binding.tipMiddleName.visibility = status
        binding.tipNamePrefix.visibility = status
        binding.tipNameSuffix.visibility = status

    }

    companion object {

        var nameOptionIsClicked: Boolean = false
    }

}