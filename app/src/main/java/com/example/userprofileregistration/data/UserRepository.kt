package com.example.userprofileregistration.data

import androidx.lifecycle.LiveData

class UserRepository(private val profileDao: UserProfileDao) {
    fun getAllProfiles(): LiveData<List<UserProfile>> {
        return profileDao.getAllProfiles()
    }

    suspend fun insert(profile: UserProfile) {
        profileDao.insert(profile)
    }

    suspend fun update(profile: UserProfile) {
        profileDao.update(profile)
    }

    suspend fun delete(profile: UserProfile) {
        profileDao.delete(profile)
    }
}