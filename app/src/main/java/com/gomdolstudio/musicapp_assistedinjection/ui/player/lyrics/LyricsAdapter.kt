package com.gomdolstudio.musicapp_assistedinjection.ui.player.lyrics

import android.util.Log
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.gomdolstudio.musicapp_assistedinjection.BR
import com.gomdolstudio.musicapp_assistedinjection.R
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Lyric
import com.gomdolstudio.musicapp_assistedinjection.util.ViewBindingHolder
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class LyricsAdapter @Inject constructor() : RecyclerView.Adapter<ViewBindingHolder<*>>() {
    private val items: MutableList<Lyric> = ArrayList<Lyric>()

    // 레이아웃 종류
    override fun getItemViewType(position: Int): Int {
        return R.layout.view_lyrics
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder<*> {
        return ViewBindingHolder<ViewDataBinding?>(parent.context, viewType)
    }
    override fun onBindViewHolder(holder: ViewBindingHolder<*>, position: Int) {
        holder.binding!!.setVariable(BR.item, items[position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    fun setItems(items: ArrayList<Lyric>?) {
        this.items.clear()
        this.items.addAll(items!!)
        notifyDataSetChanged()
    }

    fun getLyricsList(): ArrayList<Lyric>{
        return items as ArrayList<Lyric>
    }
}
