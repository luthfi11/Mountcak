package com.wysiwyg.mountcak.ui.rental

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Rental
import com.wysiwyg.mountcak.ui.rentaldetail.RentalDetailActivity
import kotlinx.android.synthetic.main.item_rental.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class RentalAdapter(private val rental: MutableList<Rental?>) : RecyclerView.Adapter<RentalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rental,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return rental.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(rental[position])
        setAnimation(holder.itemView, position)
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

        fun bindItem(rental: Rental?) {
            Glide.with(itemView.context).load(rental?.storePict).placeholder(R.drawable.logo_green).into(itemView.imgRental)

            itemView.tvStoreName.text = rental?.storeName
            itemView.tvOpenHour.text = rental?.openHour
            itemView.tvCity.text =
                String.format(itemView.context.getString(R.string.mount_location), rental?.city, rental?.region)

            itemView.viewRental.onClick { itemView.context.startActivity<RentalDetailActivity>("rental" to rental) }
        }
    }
}