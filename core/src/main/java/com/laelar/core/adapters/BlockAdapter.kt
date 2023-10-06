package com.laelar.core.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laelar.core.R
import com.laelar.core.databinding.ViewBlockBinding
import com.laelar.core.glow
import com.laelar.core.models.Block
import com.naulian.anhance.copyString
import com.naulian.anhance.loadWithGlide
import com.naulian.anhance.onClick
import com.naulian.anhance.onLongClick
import com.naulian.anhance.setTextColorRes
import com.naulian.anhance.textTrim
import com.naulian.glow.CodeTheme

class BlockAdapter : ListAdapter<Block, BlockAdapter.ItemViewHolder>(ItemDiffCallBack()) {
    private var btnListener: (Block.() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewBlockBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ItemViewHolder(private val binding: ViewBlockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val context: Context = binding.root.context

        private val defaultBg = context.getColor(R.color.tertiary)
        private val attrColor = intArrayOf(com.google.android.material.R.attr.colorTertiary)
        private val typedArray = context.theme.obtainStyledAttributes(attrColor)
        private val primaryColor = typedArray.getColor(0, defaultBg)

        init {
            typedArray.recycle()
        }

        fun bind(position: Int) {
            val item = getItem(position)
            val imageUri = item.imageUri(context)

            binding.apply {
                val color = if (item.changed) R.color.changed else R.color.background
                root.setBackgroundResource(color)

                //setVisibility
                textHeader.isVisible = item.header.isNotEmpty()
                textTitle.isVisible = item.title.isNotEmpty()
                textLabel.isVisible = item.label.isNotEmpty()

                cardImage.isVisible = item.image.isNotEmpty() || item.drawable != 0
                textDescription.isVisible = item.description.isNotEmpty()

                textSubHeader.isVisible = item.subHeader.isNotEmpty()
                textSubTitle.isVisible = item.subTitle.isNotEmpty()
                textBody.isVisible = item.body.isNotEmpty()
                textNote.isVisible = item.note.isNotEmpty()

                btnIcon.isVisible = item.link.isNotEmpty() && item.hyper.isEmpty()
                btnLink.isVisible = item.hyper.isNotEmpty()

                cardCode.isVisible = item.code.isNotEmpty()
                cardQuote.isVisible = item.quote.isNotEmpty()

                textExtra.isVisible = item.extra.isNotEmpty()

                fieldEdit.isVisible = item.hint.isNotEmpty()
                buttonAction.isVisible = item.button.isNotEmpty()

                val visibleCodeHeader = item.copyable || item.codeName.isNotEmpty()
                textLanguage.isVisible = visibleCodeHeader
                buttonCopy.isVisible = visibleCodeHeader

                //setValue
                textHeader.text = item.header
                textTitle.text = item.title
                textLabel.text = item.label

                if (item.centered) textHeader.gravity = Gravity.CENTER
                else textHeader.gravity = Gravity.START

                imageView.loadWithGlide(imageUri)
                textDescription.text = item.description

                btnIcon.icon = ContextCompat.getDrawable(context, item.icon)
                btnIcon.onClick { item.onLink?.invoke(item.link) }
                btnLink.text = item.hyper
                btnLink.icon = ContextCompat.getDrawable(context, item.icon)
                btnLink.onClick { item.onLink?.invoke(item.link) }

                if(item.language.lowercase() == "link"){
                    textCode.setTextColorRes(R.color.primary)
                }else textCode.setTextColorRes(R.color.onBackground)

                textCode.text = item.codeSpanned
                if (item.codeSpanned.isEmpty()) {
                    val colored = item.glow(CodeTheme.kotlinLight)
                    textCode.text = colored.spanned
                }

                textLanguage.setBackgroundColor(primaryColor)
                textLanguage.text = item.codeName.ifEmpty { item.language }
                buttonCopy.setIconResource(item.copyIcon)
                buttonCopy.onClick {
                    item.onCopy?.invoke(item) ?: context.copyString(item.code)
                }

                textQuote.text = item.quoteText
                cardQuote.onLongClick { context.copyString(item.quoteText) }

                textSubHeader.text = item.subHeader
                textSubTitle.text = item.subTitle

                textBody.text = item.body
                textExtra.text = item.extra

                textNote.text = item.note

                fieldEdit.helperText = item.helper
                editText.hint = item.hint

                buttonAction.text = item.button
                buttonAction.isEnabled = item.enable
                buttonAction.onClick {
                    val input = editText.textTrim()
                    val block = item.copy(input = input)
                    btnListener?.invoke(block)
                }
            }
        }
    }

    fun onClick(actionOnClick: Block.() -> Unit) {
        btnListener = actionOnClick
    }

    class ItemDiffCallBack : DiffUtil.ItemCallback<Block>() {
        override fun areItemsTheSame(oldItem: Block, newItem: Block) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Block, newItem: Block) =
            oldItem == newItem
    }
}