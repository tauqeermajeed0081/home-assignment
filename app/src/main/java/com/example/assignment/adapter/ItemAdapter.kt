package com.example.assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.model.DateModel
import com.example.assignment.model.ItemModel

class ItemAdapter(
    private val context: Context,
    private var dataList: ArrayList<ItemModel>,
    private var itemClickListener: ItemItemClickListener,
    private val itemAtPos: DateModel,
    private val positionOfDate: Int
) :

    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.expenses_detail_item_list,
                null
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val itemAtPos = dataList[position]
            itemName?.text = itemAtPos.item
            itemPrice?.text = context.getString(R.string.dollar_amount, itemAtPos.price)
            removeIcon?.setOnClickListener {
                itemClickListener.onItemClick(position, itemAtPos, positionOfDate)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView? = itemView.findViewById(R.id.transactionItem)
        var itemPrice: TextView? = itemView.findViewById(R.id.itemPrice)
        var removeIcon: ImageView? = itemView.findViewById(R.id.removeIcon)
    }

    interface ItemItemClickListener {
        fun onItemClick(position: Int, itemAtPos: ItemModel, positionOfDate: Int)
    }
}