package com.example.assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.model.DateModel

class DateAdapter (
    private val context: Context,
    private var dataList: ArrayList<DateModel>,
):

    RecyclerView.Adapter<DateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.date_item_list,
                null
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder)
        {
            val itemAtPos = dataList[position]
            date?.text = itemAtPos.date
            itemListRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val itemListAdapter =
                ItemAdapter(
                    context,
                    itemAtPos.itemList
                )
            itemListRv.adapter = itemListAdapter
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView? = itemView.findViewById(R.id.date)
        var itemListRv: RecyclerView = itemView.findViewById(R.id.nestedRv)
    }
}