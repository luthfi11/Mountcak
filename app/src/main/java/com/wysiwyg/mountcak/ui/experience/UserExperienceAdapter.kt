package com.wysiwyg.mountcak.ui.experience

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Experience
import com.wysiwyg.mountcak.util.DateUtil
import com.wysiwyg.mountcak.util.FirebaseUtil
import kotlinx.android.synthetic.main.item_experience_more.view.*

class UserExperienceAdapter(private val experiences: MutableList<Experience?>): RecyclerView.Adapter<UserExperienceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_experience_more, parent, false))
    }

    override fun getItemCount(): Int {
        return experiences.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(experiences[position])
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindItem(experience: Experience?) {
            FirebaseUtil.getUserData(itemView.context, experience?.userId, itemView.tvUserName, itemView.imgUser)
            itemView.tvTime.text = DateUtil.timeFormat("dd MMMM yyyy, HH:mm", experience?.timeStamp!!)
            itemView.tvExperience.text = experience.experience
        }
    }
}