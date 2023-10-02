package com.laelar.core.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laelar.core.R
import com.laelar.core.databinding.ViewChapterBinding
import com.laelar.core.models.Chapter
import com.laelar.core.randomEmoji
import com.naulian.anhance.onClick

class ChapterAdapter : ListAdapter<Chapter, ChapterAdapter.ItemViewHolder>(ItemDiffCallBack()) {
    private var listener: ((Chapter) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewChapterBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ItemViewHolder(private val binding: ViewChapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context: Context = binding.root.context
        private val defaultBg = context.getColor(R.color.surface)
        private val learnedBg = context.getColor(R.color.tertiary)

        private val attrColor = intArrayOf(com.google.android.material.R.attr.colorTertiary)
        private val typedArray = context.theme.obtainStyledAttributes(attrColor)
        private val primaryColor = typedArray.getColor(0, learnedBg)

        init {
            typedArray.recycle()
        }

        fun bind(position: Int) {
            val item = getItem(position)
            val bgColor = if(item.learned) primaryColor else defaultBg

            val description = if(item.learned) "Learned" else randomEmoji()

            binding.apply {
                textTitle.text = item.title
                textBody.text = description

                redIndicator.isVisible = item.new
                yellowIndicator.isVisible = item.changed

                root.setCardBackgroundColor(bgColor)
                root.onClick { listener?.invoke(item) }
            }
        }
    }

    fun onClick(actionOnClick: (Chapter) -> Unit) {
        listener = actionOnClick
    }

    class ItemDiffCallBack : DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter) =
            oldItem.path == newItem.path
    }
}