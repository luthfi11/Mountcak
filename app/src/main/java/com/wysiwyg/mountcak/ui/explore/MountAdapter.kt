package com.wysiwyg.mountcak.ui.explore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(mount: Mount?) {

            Glide.with(itemView.context).load(mount?.cover).placeholder(R.color.colorMuted).into(itemView.imgMount)
            itemView.tvMountName.text = mount?.mountName
            itemView.tvHeight.text = mount?.height
            itemView.tvCity.text = mount?.city+", "
            itemView.tvRegion.text = mount?.region

            itemView.btnDetail.onClick {itemView.context.startActivity<MountDetailActivity>("mount" to mount) }
        }
    }
}