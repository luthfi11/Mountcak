package com.wysiwyg.mountcak.ui.mountdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Experience
import com.wysiwyg.mountcak.util.FirebaseUtil
import kotlinx.android.synthetic.main.item_experience.view.*

class ExperienceAdapter(private val experiences: MutableList<Experience?>): RecyclerView.Adapter<ExperienceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_experience, parent, false))
    }

    override fun getItemCount(): Int {
        return experiences.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(experiences[position])
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindItem(experience: Experience?) {
            if (experience?.userId != null) {
                FirebaseUtil.getUserData(itemView.context, experience.userId, itemView.tvUserName, itemView.imgUser)
                itemView.tvExperience.text = experience.experience
            }
        }
    }
}