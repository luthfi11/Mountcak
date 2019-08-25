package com.wysiwyg.mountcak.ui.mountdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.viewphoto.ViewPhotoActivity
import kotlinx.android.synthetic.main.item_photo.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class PhotoAdapter(private val photos: MutableList<String?>) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false))
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(photos[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(photo: String?) {
            Glide.with(itemView.context).load(photo).placeholder(R.color.colorMuted).into(itemView.imgPhoto)
            itemView.onClick { itemView.context.startActivity<ViewPhotoActivity>("photo" to photo) }
        }
    }
}