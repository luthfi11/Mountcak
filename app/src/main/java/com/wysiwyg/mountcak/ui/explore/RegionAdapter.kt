package com.wysiwyg.mountcak.ui.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wysiwyg.mountcak.R
import kotlinx.android.synthetic.main.item_region.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColorResource

class RegionAdapter(private val regions: List<String?>, private val regionView: RegionView): RecyclerView.Adapter<RegionAdapter.ViewHolder>() {

    var regionPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_region,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return regions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(regions[position])
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindItem(region: String?) {
            itemView.tvRegion.text = region
            itemView.tvRegion.onClick {
                regionPos = adapterPosition
                regionView.onRegionChange(region!!)
                notifyDataSetChanged()
            }
            if (regionPos == adapterPosition) {
                itemView.cvRegion.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorPrimaryDark
                    )
                )
                itemView.tvRegion.textColorResource = android.R.color.white
            } else {
                itemView.cvRegion.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.defaultBg
                    )
                )
                itemView.tvRegion.textColorResource = android.R.color.tab_indicator_text
            }
        }
    }
}