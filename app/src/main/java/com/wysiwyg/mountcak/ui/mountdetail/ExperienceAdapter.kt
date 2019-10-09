package com.wysiwyg.mountcak.ui.mountdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Review
import com.wysiwyg.mountcak.util.FirebaseUtil
import kotlinx.android.synthetic.main.item_experience.view.*

class ExperienceAdapter(private val reviews: MutableList<Review?>): RecyclerView.Adapter<ExperienceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_experience, parent, false))
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(reviews[position])
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindItem(review: Review?) {
            if (review?.userId != null) {
                FirebaseUtil.getUserData(itemView.context, review.userId, itemView.tvUserName, itemView.imgUser)
                itemView.tvExperience.text = review.experience
            }
        }
    }
}