package com.wysiwyg.mountcak.ui.mountgallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.viewphoto.ViewPhotoActivity
import kotlinx.android.synthetic.main.item_gallery.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class GalleryAdapter(private val photos: MutableList<String?>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false))
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(photos[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(photo: String?) {
            Glide.with(itemView.context).load(photo).into(itemView.imgMount)
            itemView.imgMount.onClick { itemView.context.startActivity<ViewPhotoActivity>("photo" to photo) }
        }
    }
}