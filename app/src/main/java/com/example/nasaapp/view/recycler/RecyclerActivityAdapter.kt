package com.example.nasaapp.view.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaapp.databinding.ActivityRecyclerItemEarthBinding
import com.example.nasaapp.databinding.ActivityRecyclerItemFooterBinding
import com.example.nasaapp.databinding.ActivityRecyclerItemHeaderBinding
import com.example.nasaapp.databinding.ActivityRecyclerItemMarsBinding

class RecyclerActivityAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Pair<Data, Boolean>>
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_EARTH -> {
                val binding: ActivityRecyclerItemEarthBinding =
                    ActivityRecyclerItemEarthBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                EarthViewHolder(binding.root)
            }
            TYPE_MARS -> {
                val binding: ActivityRecyclerItemMarsBinding =
                    ActivityRecyclerItemMarsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                MarsViewHolder(binding.root)
            }
            TYPE_HEADER -> {
                val binding: ActivityRecyclerItemHeaderBinding =
                    ActivityRecyclerItemHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                HeaderViewHolder(binding.root)
            }

            TYPE_FOOTER -> {
                val binding: ActivityRecyclerItemFooterBinding =
                    ActivityRecyclerItemFooterBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                FooterViewHolder(binding.root)
            }

            else -> throw IllegalStateException("Invalid ViewType")

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            data.lastIndex -> TYPE_FOOTER
            else -> {
                when (data[position].first.someDescription.isBlank()) {
                    true -> TYPE_MARS
                    else -> TYPE_EARTH
                }
            }
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder).bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class EarthViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(pairData: Pair<Data, Boolean>) {
            ActivityRecyclerItemEarthBinding.bind(itemView).apply {
                descriptionTextView.text = pairData.first.someDescription
                wikiImageView.setOnClickListener {
                    onListItemClickListener.onItemClick(pairData.first)
                }
            }
        }
    }

    private var lastToggledItem: Int = -1
    inner class MarsViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(pairData: Pair<Data, Boolean>) {
            ActivityRecyclerItemMarsBinding.bind(itemView).apply {
                marsTextView.apply {
                    text = pairData.first.someText
                    setOnClickListener {
                        toggleText()
                    }
                }
                marsImageView.setOnClickListener {
                    onListItemClickListener.onItemClick(pairData.first)
                }
                addItemImageView.setOnClickListener {
                    addItem()
                }
                removeItemImageView.setOnClickListener {
                    removeItem()
                }
                moveItemUp.setOnClickListener {
                    moveUp()
                }

                moveItemDown.setOnClickListener {
                    moveDown()
                }
                marsDescriptionTextView.visibility =
                    if (pairData.second) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
        }


        private fun toggleText() {
            lastToggledItem.takeIf { it >= 0 }?.apply {
                data[this] = data[this].let {
                    it.first to !it.second
                }
                notifyItemChanged(lastToggledItem)
            }

            data[layoutPosition] = data[layoutPosition].let{
                it.first to !it.second
            }
            lastToggledItem = layoutPosition
            notifyItemChanged(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < itemCount - 2 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun addItem() {
            data.add(layoutPosition, generateItem() to false)
            notifyItemInserted(layoutPosition)
            notifyItemRangeChanged(layoutPosition + 1, data.size - 1)
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
            notifyItemRangeChanged(layoutPosition, data.size - 1)
        }
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(pairData: Pair<Data, Boolean>) {
            ActivityRecyclerItemHeaderBinding.bind(itemView).apply {
                root.setOnClickListener {
                    onListItemClickListener.onItemClick(pairData.first)
                }
            }
        }
    }

    inner class FooterViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(pairData: Pair<Data, Boolean>) {
            ActivityRecyclerItemFooterBinding.bind(itemView).apply {
                footer.setOnClickListener {
                    onListItemClickListener.onItemClick(pairData.first)
                }
            }
        }

    }

    fun appendItem() {
        val insertPosition = data.lastIndex
        data.add(insertPosition, generateItem() to false)
        notifyItemInserted(insertPosition + 1)
    }

    private var i = 0
    private fun generateItem(): Data {
        return Data("Mars ${i++}", "")
    }

    init {
        repeat(5) {
            appendItem()
        }
    }

    companion object {
        private const val TYPE_EARTH = 0
        private const val TYPE_MARS = 1
        private const val TYPE_HEADER = 2
        private const val TYPE_FOOTER = 3
    }


}