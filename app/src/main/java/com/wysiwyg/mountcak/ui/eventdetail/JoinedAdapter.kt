package com.wysiwyg.mountcak.ui.eventdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.ui.userdetail.UserDetailActivity
import com.wysiwyg.mountcak.util.FirebaseUtil
import kotlinx.android.synthetic.main.item_joined.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class JoinedAdapter(private val user: MutableList<User?>) : RecyclerView.Adapter<JoinedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_joined, parent, false))
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(user[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(user: User?) {
            Glide.with(itemView.context).load(user?.photo).into(itemView.imgUser)
            if (user?.id != FirebaseUtil.currentUser())
                itemView.imgUser.onClick { itemView.context.startActivity<UserDetailActivity>("userId" to user?.id) }
        }
    }
}