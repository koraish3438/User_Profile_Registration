    package com.example.userprofileregistration.ui
    
    import android.os.Bundle
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import com.example.userprofileregistration.data.UserProfile
    import com.example.userprofileregistration.databinding.ActivitySingleProfileBinding
    import com.example.userprofileregistration.viewmodel.UserProfileViewModel
    
    class SingleProfileActivity : AppCompatActivity() {
        private lateinit var binding: ActivitySingleProfileBinding
        private lateinit var viewModel: UserProfileViewModel
        private lateinit var currentProfile: UserProfile
        private var profileId: Int = 0
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            binding = ActivitySingleProfileBinding.inflate(layoutInflater)
            setContentView(binding.root)
    
            viewModel = UserProfileViewModel(application)
    
            profileId = intent.getIntExtra("profileId", 0)
    
            viewModel.getAllProfiles().observe(this) { profiles ->
                currentProfile = profiles.find { it.id == profileId } ?: return@observe
    
                binding.textName.text = currentProfile.name
                binding.textEmail.text = currentProfile.email
                binding.textDob.text = currentProfile.dob
                binding.textNumber.text = currentProfile.number
                binding.textDistrict.text = currentProfile.district
            }
        }
    }