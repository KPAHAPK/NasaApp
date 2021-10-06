package com.example.nasaapp.view.recycler.recycler

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ActivityRecyclerItemEarthBinding
import com.example.nasaapp.databinding.ActivityRecyclerItemFooterBinding
import com.example.nasaapp.databinding.ActivityRecyclerItemHeaderBinding
import com.example.nasaapp.databinding.ActivityRecyclerItemMarsBinding

class RecyclerActivityAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var onStartDragListener: OnStartDragListener,
    private var data: MutableList<Pair<Data, Boolean>>
) : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {

    companion object {
        private const val TYPE_EARTH = 0
        private const val TYPE_MARS = 1
        private const val TYPE_HEADER = 2
        private const val TYPE_FOOTER = 3
    }

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


    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val combinedChange =
                createCombinedPayload(payloads as List<Change<Pair<Data, Boolean>>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.first.someText != oldData.first.someText) {
                holder.itemView.findViewById<TextView>(R.id.marsTextView).text =
                    data[position].first.someText
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }


    override fun getItemCount(): Int {
        return data.size
    }


    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(pairData: Pair<Data, Boolean>) {
            ActivityRecyclerItemHeaderBinding.bind(itemView).apply {
                root.setOnClickListener {
                    data[1] = Pair(Data("Jupiter", ""), false)
                    notifyItemChanged(1, Pair(Data("", ""), false))
                    //onListItemClickListener.onItemClick(pairData.first)
                }
            }
        }
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

                dragHandleImageView.setOnTouchListener { view, motionEvent ->
                    if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                        onStartDragListener.onStartDrag(this@MarsViewHolder)
                    }
                    false
                }
            }
        }

        private fun toggleText() {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
            }

            val isTheSamePosition = lastToggledItem == layoutPosition
            if (isTheSamePosition) {
                lastToggledItem = View.NO_ID
            } else {
                lastToggledItem.takeIf { it >= 0 }?.apply {
                    data[this] = data[this].let {
                        it.first to !it.second
                    }
                    notifyItemChanged(lastToggledItem)
                }
                lastToggledItem = layoutPosition
            }
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
            data.add(layoutPosition + 1, generateItem() to false)
            notifyItemInserted(layoutPosition + 1)
            notifyItemRangeChanged(layoutPosition + 1, data.size - 1)
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
            notifyItemRangeChanged(layoutPosition, data.size - 1)
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


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition != 0 && fromPosition != data.lastIndex && toPosition != 0 && toPosition != data.lastIndex) {
            data.removeAt(fromPosition).apply {
                data.add(toPosition, this)
            }
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setItems(newItems: List<Pair<Data, Boolean>>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(data, newItems))
        result.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newItems)
    }


}