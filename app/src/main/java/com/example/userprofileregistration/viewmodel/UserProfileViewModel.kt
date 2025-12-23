package com.example.userprofileregistration.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.userprofileregistration.data.UserProfile
import com.example.userprofileregistration.data.UserProfileDatabase
import com.example.userprofileregistration.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application): AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val profileDao = UserProfileDatabase.getDatabase(application).profileDao()
        repository = UserRepository(profileDao)
    }

    fun getAllProfiles(): LiveData<List<UserProfile>> {
        return repository.getAllProfiles()
    }

    fun insertProfile(profile: UserProfile) {
        viewModelScope.launch ( Dispatchers.IO ) {
            repository.insert(profile)
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch ( Dispatchers.IO ) {
            repository.update(profile)
        }
    }

    fun deleteProfile(profile: UserProfile) {
        viewModelScope.launch ( Dispatchers.IO ) {
            repository.delete(profile)
        }
    }
}