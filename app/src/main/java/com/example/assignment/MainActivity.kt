package com.example.assignment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.adapter.DateAdapter
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.model.DateModel
import com.example.assignment.model.ItemData
import com.example.assignment.model.ItemModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    private var dateAdapter: DateAdapter? = null
    private var dateModelArray: ArrayList<DateModel> = ArrayList()
    private var itemListModelArray: ArrayList<ItemModel> = ArrayList()
    var dialog: Dialog? = null
    private var itemData: ArrayList<ItemData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addButton.setOnClickListener(this)
        populateData()
        setUpRV()
    }

    private fun setUpRV() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.layoutManager = layoutManager
        dateAdapter = DateAdapter(this, dateModelArray)
        binding.rvList.adapter = dateAdapter
    }

    private fun populateData() {
        for (i in 0..5) {
            itemListModelArray.add(ItemModel("Grocery", 100))
        }
        dateModelArray.add(DateModel(getCurrentTimeStamp(), itemListModelArray))
        dateAdapter = DateAdapter(
            this,
            dateModelArray
        )
        dateAdapter?.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.addButton -> {
                alertDialog()
            }
        }
    }

    private fun alertDialog() {
        itemData.clear()
        itemData.add(ItemData(1, "Expense"))
        itemData.add(ItemData(2, "Income"))
        val adapter: ArrayAdapter<ItemData> = ArrayAdapter<ItemData>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            itemData
        )

        dialog = Dialog(this)
        dialog?.setContentView(R.layout.popup_dialog)
        dialog?.setCancelable(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val spinner = dialog?.findViewById<Spinner>(R.id.mySpinner)
        spinner?.adapter = adapter

        spinner?.onItemSelectedListener = this
        dialog?.show()

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}