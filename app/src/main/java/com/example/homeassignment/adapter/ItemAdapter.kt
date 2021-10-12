package com.example.homeassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homeassignment.model.ItemModel
import com.example.homeassignment.R

class ItemAdapter(
    private val context: Context,
    private var dataList: ArrayList<ItemModel>
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

    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        with(holder)
        {
            val itemAtPos = dataList[position]
            itemName?.text = itemAtPos.item
            itemPrice?.text = itemAtPos.price.toString()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView? = itemView.findViewById(R.id.transactionItem)
        var itemPrice: TextView? = itemView.findViewById(R.id.itemPrice)
    }
}