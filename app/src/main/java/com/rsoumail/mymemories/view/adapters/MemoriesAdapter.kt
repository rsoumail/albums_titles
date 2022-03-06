package com.rsoumail.mymemories.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.rsoumail.mymemories.R
import com.rsoumail.mymemories.databinding.MemoryItemBinding
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.utils.USER_AGENT

class MemoriesAdapter(private val onMemoryClickListener: OnMemoryClickListener) : PagingDataAdapter<Memory, MemoriesAdapter.MemoryViewHolder>(Companion) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MemoryItemBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return MemoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoryViewHolder, position: Int) {
        val memory = getItem(position) ?: return
        holder.bind(memory, onMemoryClickListener.clickListener)
    }

    companion object : DiffUtil.ItemCallback<Memory>() {
        override fun areItemsTheSame(oldItem: Memory, newItem: Memory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Memory, newItem: Memory): Boolean {
            return oldItem == newItem
        }
    }

    inner class MemoryViewHolder(
        private val binding: MemoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(memory: Memory, clickListener: (memory: Memory) -> Unit) {
            binding.memoryTitleTextView.text = memory.title
            Glide.with(itemView.context)
                .asBitmap()
                .load(GlideUrl(memory.thumbnailUrl, LazyHeaders.Builder().addHeader("User-Agent", USER_AGENT).build()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error)
                .into(binding.memoryIV)
            binding.memoryContainer.setOnClickListener { clickListener(memory) }
        }
    }
}

data class OnMemoryClickListener(val clickListener: (memory: Memory) -> Unit)