package com.example.userprofileregistration.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userprofileregistration.data.UserProfile
import com.example.userprofileregistration.databinding.UserProfileItemBinding

class ProfileAdapter(private var profile: List<UserProfile>,
                     private val onItemClicked: (UserProfile) -> Unit,
                     private val onEditClick: (UserProfile) -> Unit,
                     private val onDeleteClick: (UserProfile) -> Unit
): RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    fun updateList(newList: List<UserProfile>) {
        profile = newList
        notifyDataSetChanged()
    }

    inner class ProfileViewHolder(val binding: UserProfileItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = UserProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val currentProfile = profile[position]
        holder.binding.tvName.text = currentProfile.name
        holder.binding.tvEmail.text = currentProfile.email
        holder.binding.tvNumber.text = currentProfile.number
        holder.binding.tvDob.text = currentProfile.dob
        holder.binding.tvDistrict.text = currentProfile.district

        holder.binding.root.setOnClickListener {
            onItemClicked(currentProfile)
        }

        holder.binding.btnEdit.setOnClickListener {
            onEditClick(currentProfile)
        }

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(currentProfile)
        }
    }

    override fun getItemCount(): Int = profile.size
}