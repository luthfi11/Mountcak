package com.wysiwyg.mountcak.ui.explore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.ui.mountdetail.MountDetailActivity
import kotlinx.android.synthetic.main.item_mount.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class MountAdapter(private val mounts: MutableList<Mount?>) : RecyclerView.Adapter<MountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.item_mount,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mounts.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItem(mounts[p1])
        setAnimation(p0.itemView, p1)
    }

    private var lastPosition = -1
    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(mount: Mount?) {

            Glide.with(itemView.context).load(mount?.cover).placeholder(R.color.colorMuted).into(itemView.imgMount)
            itemView.tvMountName.text = mount?.mountName
            itemView.tvHeight.text = mount?.height
            itemView.tvCity.text = String.format(itemView.context.getString(R.string.mount_location), mount?.city, mount?.region)

            itemView.btnDetail.onClick {
                itemView.context.startActivity<MountDetailActivity>("mountId" to mount?.id)
            }
        }
    }
}