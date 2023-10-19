package com.laelar.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laelar.core.R
import com.laelar.core.databinding.ViewBookBinding
import com.laelar.core.models.Book
import com.naulian.anhance.onClick

class BookAdapter : ListAdapter<Book, BookAdapter.ItemViewHolder>(ItemDiffCallBack()) {
    private var listener: ((Book) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewBookBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ItemViewHolder(private val binding: ViewBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        private val defaultBg = context.getColor(R.color.tertiary)
        private val attrColor = intArrayOf(com.google.android.material.R.attr.colorTertiary)
        private val typedArray = context.theme.obtainStyledAttributes(attrColor)
        private val primaryColor = typedArray.getColor(0, defaultBg)

        init {
            typedArray.recycle()
        }

        fun bind(position: Int) {
            val item = getItem(position)
            val progress = item.chapters.count { it.learned }

            binding.apply {
                imageCourse.isVisible = false
                //imageCourse.loadWithGlide(item.image)
                textTitle.text = item.name
                textBody.text = item.description

                indicator.setIndicatorColor(primaryColor)
                indicator.max = item.chapters.size
                indicator.progress = progress

                redIndicator.isVisible = item.new
                yellowIndicator.isVisible = item.changed

                root.onClick { listener?.invoke(item) }
            }
        }
    }

    fun onClick(actionOnClick: (Book) -> Unit) {
        listener = actionOnClick
    }

    class ItemDiffCallBack : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Book, newItem: Book) =
            oldItem.path == newItem.path
    }
}