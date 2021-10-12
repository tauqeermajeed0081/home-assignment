package com.example.homeassignment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homeassignment.adapter.DateAdapter
import com.example.homeassignment.databinding.ActivityMainBinding
import com.example.homeassignment.model.DateModel
import com.example.homeassignment.model.ItemModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.ArrayAdapter
import com.example.homeassignment.model.ItemData
import com.google.android.material.internal.ContextUtils.getActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {
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

    private fun setUpRV(){
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.layoutManager = layoutManager
        dateAdapter = DateAdapter(this, dateModelArray)
        binding.rvList.adapter = dateAdapter
    }

    private fun populateData(){
        for (i in 0..5){
            itemListModelArray.add(ItemModel("Grocery",100))
        }
        dateModelArray.add(DateModel(getCurrentTimeStamp(), itemListModelArray))
        dateAdapter = DateAdapter(
            this,
            dateModelArray
        )
        dateAdapter?.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when(v){
            binding.addButton ->{

            }
        }
    }

    private fun alertDialog() {

        itemData.clear()
        itemData.add(ItemData())
        val adapter: ArrayAdapter<ItemData> = ArrayAdapter<ItemData>(
           this,
            android.R.layout.simple_spinner_dropdown_item,
            itemData
        )

        dialog = BottomSheetDialog(this)
        dialog?.setContentView(R.layout.popup_dialog)
        dialog?.setCancelable(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val spinner = dialog?.findViewById<TextView>(R.id.mySpinner)
//        spinner.setAdapter(adapter)

        spinner?.setOnClickListener {

        }

        dialog?.show()

    }
}