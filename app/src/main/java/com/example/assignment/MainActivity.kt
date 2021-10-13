package com.example.assignment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.adapter.DateAdapter
import com.example.assignment.adapter.ItemAdapter
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.model.DateModel
import com.example.assignment.model.ItemData
import com.example.assignment.model.ItemModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnClickListener, ItemAdapter.ItemItemClickListener {
    lateinit var binding: ActivityMainBinding
    private var dateAdapter: DateAdapter? = null
    private var dateModelArray: ArrayList<DateModel> = ArrayList()
    private var itemListModelArray: ArrayList<ItemModel> = ArrayList()
    var dialog: Dialog? = null
    private var itemData: ArrayList<ItemData> = ArrayList()
    private var TAG = "MainActivity"
    private lateinit var dateEditTxt: TextInputEditText
    private var sdf: SimpleDateFormat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addButton.setOnClickListener(this)
        //populateData()
        setUpRV()
    }

    private fun setUpRV() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.layoutManager = layoutManager
        dateAdapter = DateAdapter(this, dateModelArray, this)
        binding.rvList.adapter = dateAdapter
    }

    private fun populateData() {
        for (i in 0..5) {
            itemListModelArray.add(ItemModel("Grocery", 100))
        }
        dateModelArray.add(DateModel(getCurrentTimeStamp(), itemListModelArray))
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
        val txtItemName = dialog?.findViewById<TextInputEditText>(R.id.txtItemName)
        val descriptionEditText = dialog?.findViewById<TextInputLayout>(R.id.descriptionEditText)
        dateEditTxt = dialog?.findViewById(R.id.edtDate)!!
        dateEditTxt.setText(getCurrentTimeStamp())
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
                if (position == 0) {
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
        dateEditTxt.setOnClickListener {
            dateEditTxt.transformIntoDatePicker(this, "dd'th' MMMM, yyyy")
            dateEditTxt.transformIntoDatePicker(this, "dd'th' MMMM, yyyy", Date())
        }
        addBtn?.setOnClickListener {
            when {
                spinner?.selectedItemPosition == 0 -> {
                    Toast.makeText(this, "Please select the Transaction Type", Toast.LENGTH_SHORT)
                        .show()
                }
                txtItemName?.text.isNullOrEmpty() -> {
                    descriptionEditText?.error = resources.getString(R.string.description_item)
                }
                else -> {
                    dialog?.dismiss()
                    val indexFound = containsSameDate(dateEditTxt.text.toString(), dateModelArray)
                    if (indexFound != -1) {
                        // already exist
                        val prevItemList = dateModelArray[indexFound].itemList
                        prevItemList.add(
                            ItemModel(
                                txtItemName?.text.toString(),
                                incDecValue?.text.toString().toInt()
                            )
                        )
                    } else {
                        // not already exist
                        val arrayListOfItem = ArrayList<ItemModel>()
                        val item = ItemModel()
                        item.item = txtItemName?.text.toString()
                        item.price = incDecValue?.text.toString().toInt()
                        arrayListOfItem.add(item)
                        dateModelArray.add(DateModel(dateEditTxt.text.toString(), arrayListOfItem))
                    }
                    dateAdapter?.notifyDataSetChanged()
                }
            }
        }
        dialog?.show()
    }

    override fun onItemClick(position: Int, itemAtPos: ItemModel, positionOfDate: Int) {
        val tempDateArrayObj = dateModelArray[positionOfDate]
        val tempItemArray = tempDateArrayObj.itemList
        tempItemArray.remove(itemAtPos)
        if (tempItemArray.size == 0) dateModelArray.remove(tempDateArrayObj)

        dateAdapter?.notifyDataSetChanged()
    }

    private fun EditText.transformIntoDatePicker(
        context: Context,
        format: String,
        maxDate: Date? = null
    ) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        dateEditTxt.hideKeyboard()
        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf?.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }

    private fun containsSameDate(
        date: String?,
        list: ArrayList<DateModel>,
    ): Int {
        for ((index, obj) in list.withIndex()) {
            if (obj.date.isNotEmpty() && obj.date == date) {
                return index
            }
        }
        return -1
    }
}