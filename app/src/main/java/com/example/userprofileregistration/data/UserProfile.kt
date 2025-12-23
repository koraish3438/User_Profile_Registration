package com.example.userprofileregistration.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var email: String,
    var dob: String,
    var district: String,
    var number: String,
): Serializable