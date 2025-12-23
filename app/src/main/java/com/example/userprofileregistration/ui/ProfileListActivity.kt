package com.example.userprofileregistration.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userprofileregistration.R
import com.example.userprofileregistration.adapter.ProfileAdapter
import com.example.userprofileregistration.databinding.ActivityProfileListBinding
import com.example.userprofileregistration.viewmodel.UserProfileViewModel

class ProfileListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileListBinding
    private lateinit var profileViewModel: UserProfileViewModel
    private lateinit var profileAdapter: ProfileAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileViewModel = UserProfileViewModel(application)


        profileAdapter = ProfileAdapter(
            emptyList(),
            onItemClicked = { profile ->
                val intent = Intent(this, SingleProfileActivity::class.java)
                intent.putExtra("profileId", profile.id)
                startActivity(intent)
            },
            onEditClick = { profile ->
                val intent = Intent(this, UpdateProfileActivity::class.java)
                intent.putExtra("profileId", profile.id)
                startActivity(intent)
            },
            onDeleteClick = { profile ->
                profileViewModel.deleteProfile(profile)
            }
        )

        binding.recyclerViewProfiles.adapter = profileAdapter
        binding.recyclerViewProfiles.layoutManager = LinearLayoutManager(this)

        profileViewModel.getAllProfiles().observe(this) { profile ->
            profileAdapter.updateList(profile)

            binding.tvProfileCounter.text = "Profiles : ${profile.size}"
        }

        binding.btnAddProfile.setOnClickListener {
            startActivity(Intent(this, AddProfileActivity::class.java))
        }
    }
}