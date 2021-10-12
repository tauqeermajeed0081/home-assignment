package com.example.assignment

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Trace.isEnabled
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.adapter.DateAdapter
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.model.DateModel
import com.example.assignment.model.ItemData
import com.example.assignment.model.ItemModel
import java.util.logging.Logger


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding
    private var dateAdapter: DateAdapter? = null
    private var dateModelArray: ArrayList<DateModel> = ArrayList()
    private var itemListModelArray: ArrayList<ItemModel> = ArrayList()
    var dialog: Dialog? = null
    private var itemData: ArrayList<ItemData> = ArrayList()
    private var TAG = "MainActivity"

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
        dialog = Dialog(this)
        dialog?.setContentView(R.layout.popup_dialog)
        dialog?.setCancelable(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val spinner = dialog?.findViewById<Spinner>(R.id.mySpinner)
        val up = dialog?.findViewById<ImageView>(R.id.up)
        val down = dialog?.findViewById<ImageView>(R.id.down)
        val incDecValue = dialog?.findViewById<EditText>(R.id.incDecValue)
        val addBtn = dialog?.findViewById<Button>(R.id.addButton)
        itemData.clear()
        itemData.add(ItemData(1, "Transaction Type"))
        itemData.add(ItemData(1, "Expense"))
        itemData.add(ItemData(2, "Income"))
        val adapter: ArrayAdapter<ItemData> = object : ArrayAdapter<ItemData>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            itemData
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                // set item text bold
                if (position === 0) {
                    // Set the hint text color gray
                    view.setTextColor(Color.GRAY)
                } else {
                    view.setTextColor(Color.BLACK)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

        }
        spinner?.adapter = adapter

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val itemData = adapter.getItem(position)
                    Log.d(TAG, "onItemSelected: ${itemData?.id}")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        up?.setOnClickListener {
            val value = incDecValue?.text.toString().toInt().plus(1)
            incDecValue?.setText(value.toString())
            incDecValue?.setSelection(incDecValue.length())
        }
        down?.setOnClickListener {
            val value = incDecValue?.text.toString().toInt().minus(1)
            if (value > 0) {
                incDecValue?.setText(value.toString())
                incDecValue?.setSelection(incDecValue.length())

            }
        }
        addBtn?.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.show()

    }
}