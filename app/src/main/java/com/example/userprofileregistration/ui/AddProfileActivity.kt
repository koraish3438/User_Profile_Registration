package com.example.userprofileregistration.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.userprofileregistration.data.UserProfile
import com.example.userprofileregistration.databinding.ActivityAddProfileBinding
import com.example.userprofileregistration.viewmodel.UserProfileViewModel

class AddProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProfileBinding
    private lateinit var viewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = UserProfileViewModel(application)

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val dob = binding.editTextDob.text.toString()
            val number = binding.editTextNumber.text.toString()
            val district = binding.editTextDistrict.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && dob.isNotEmpty() && number.isNotEmpty() && district.isNotEmpty()) {
                val profile = UserProfile(
                    name = name,
                    email = email,
                    dob = dob,
                    number = number,
                    district = district
                )
                viewModel.insertProfile(profile)
                finish()
            }
            else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }


    }
}