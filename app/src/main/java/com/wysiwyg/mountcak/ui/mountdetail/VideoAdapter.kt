package com.wysiwyg.mountcak.ui.mountdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.SearchResult

class VideoAdapter(private val videos: MutableList<SearchResult?>) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false))
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(videos[position]!!.id?.videoId)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val videoMount: YouTubePlayerView = itemView.findViewById(R.id.video_youtube)

        fun bindItem(id: String?) {

            videoMount.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(id!!, 0f)
                }
            })
        }
    }
}