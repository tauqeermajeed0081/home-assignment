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
import com.example.assignment.db.TinyDB
import com.example.assignment.model.DateModel
import com.example.assignment.model.SpinnerData
import com.example.assignment.model.ItemModel
import com.example.assignment.model.UserData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnClickListener, ItemAdapter.ItemItemClickListener {
    lateinit var binding: ActivityMainBinding
    private var dateAdapter: DateAdapter? = null
    private var dateModelArray: ArrayList<DateModel> = ArrayList()
    private var dialog: Dialog? = null
    private var itemData: ArrayList<SpinnerData> = ArrayList()
    private var TAG = "MainActivity"
    private var sdf: SimpleDateFormat? = null
    private lateinit var tinyDB: TinyDB
    private lateinit var userData: UserData
    private var balanceVal: Long = 0
    private var incomeVal: Long = 0
    private var expenseVal: Long = 0
    private var isExpense = false
    private var itemType = ""
    private lateinit var dateEditTxt: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tinyDB = TinyDB(applicationContext)
        if (!tinyDB.getBoolean("isFirstTime")) {
            saveIntoDb(0, 0, 0, null)
            tinyDB.putBoolean("isFirstTime", true)
        }
        userData = getFromDb()
        addButtonFloating.setOnClickListener(this)
        populateData()
        setUpRV()
    }

    private fun setUpRV() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvList.layoutManager = layoutManager
        dateAdapter = DateAdapter(this, dateModelArray, this)
        rvList.adapter = dateAdapter
    }

    private fun populateData() {
        expense.text = getString(R.string.balance_value, userData.userExpense)
        income.text = getString(R.string.balance_value, userData.userIncome)
        txtBalance.text = getString(R.string.balance_value, userData.userBalance)
        expenseVal = userData.userExpense
        incomeVal = userData.userIncome
        balanceVal = userData.userBalance
        if (!userData.userTransactionList.isNullOrEmpty()) {
            dateModelArray = userData.userTransactionList!!
            dateAdapter?.notifyDataSetChanged()
        }
        setProgressBar()
    }

    override fun onClick(v: View?) {
        when (v) {
            addButtonFloating -> {
                alertDialog()
            }
        }
    }

    private fun alertDialog() {
        dialog = Dialog(this)
        dialog?.setContentView(R.layout.popup_dialog)
        dialog?.setCancelable(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val mySpinner = dialog?.findViewById<Spinner>(R.id.mySpinner)
        val up = dialog?.findViewById<ImageView>(R.id.up)
        val down = dialog?.findViewById<ImageView>(R.id.down)
        val incDecValue = dialog?.findViewById<EditText>(R.id.incDecValue)
        val addButtonPopup = dialog?.findViewById<Button>(R.id.addButtonPopup)
        val txtItemName = dialog?.findViewById<TextInputEditText>(R.id.txtItemName)
        val descriptionEditText = dialog?.findViewById<TextInputLayout>(R.id.descriptionEditText)
        dateEditTxt = dialog?.findViewById(R.id.dateEditTxt)!!
        dateEditTxt.setText(getCurrentTimeStamp())
        itemData.clear()
        itemData.add(SpinnerData(0, "Transaction Type"))
        itemData.add(SpinnerData(1, "Expense"))
        itemData.add(SpinnerData(2, "Income"))
        val adapter: ArrayAdapter<SpinnerData> = object : ArrayAdapter<SpinnerData>(
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
        mySpinner?.adapter = adapter

        mySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val itemData = adapter.getItem(position)
                    Log.d(TAG, "onItemSelected: ${itemData?.id}")
                    isExpense = itemData?.id?.equals(1) == true
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
        addButtonPopup?.setOnClickListener {
            when {
                mySpinner?.selectedItemPosition == 0 -> {
                    Toast.makeText(this, "Please select the Transaction Type", Toast.LENGTH_SHORT)
                        .show()
                }
                txtItemName?.text.isNullOrEmpty() -> {
                    descriptionEditText?.error = resources.getString(R.string.description_item)
                }
                else -> {
                    dialog?.dismiss()
                    if (isExpense) {
                        expenseVal = expenseVal.plus(incDecValue?.text.toString().toLong())
                        itemType = "Expense"
                    } else {
                        incomeVal = incomeVal.plus(incDecValue?.text.toString().toLong())
                        itemType = "Income"
                    }
                    val indexFound = containsSameDate(dateEditTxt.text.toString(), dateModelArray)
                    if (indexFound != -1) {
                        // already exist
                        val prevItemList = dateModelArray[indexFound].itemList
                        prevItemList.add(
                            ItemModel(
                                txtItemName?.text.toString(),
                                incDecValue?.text.toString().toInt(),
                                itemType
                            )
                        )
                    } else {
                        // not already exist
                        val arrayListOfItem = ArrayList<ItemModel>()
                        val item = ItemModel()
                        item.item = txtItemName?.text.toString()
                        item.price = incDecValue?.text.toString().toInt()
                        item.itemType = itemType
                        arrayListOfItem.add(item)
                        dateModelArray.add(DateModel(dateEditTxt.text.toString(), arrayListOfItem))
                    }
                    dateAdapter?.notifyDataSetChanged()
                    txtBalance.text =
                        getString(R.string.balance_value, calculateBalance(incomeVal, expenseVal))
                    expense.text = getString(R.string.balance_value, expenseVal)
                    income.text = getString(R.string.balance_value, incomeVal)
                    balanceVal = calculateBalance(incomeVal, expenseVal)
                    saveIntoDb(expenseVal, incomeVal, balanceVal, dateModelArray)
                    setProgressBar()
                }
            }
        }
        dialog?.show()
    }

    private fun saveIntoDb(
        expense: Long,
        income: Long,
        balance: Long,
        transactionList: ArrayList<DateModel>?
    ) {
        tinyDB.putObject("userData", UserData(expense, income, balance, transactionList))
    }

    private fun getFromDb(): UserData {
        return tinyDB.getObject("userData", UserData::class.java)
    }

    private fun setProgressBar() {
        progressBar.max = incomeVal.toInt()
        progressBar.progress = expenseVal.toInt()
    }

    private fun calculateBalance(income: Long, expense: Long): Long {
        return income.minus(expense)
    }

    override fun onItemClick(position: Int, itemAtPos: ItemModel, positionOfDate: Int) {
        val tempDateArrayObj = dateModelArray[positionOfDate]
        val tempItemArray = tempDateArrayObj.itemList
        if (itemAtPos.itemType.equals("Expense")) {
            expenseVal = itemAtPos.price?.toLong()?.let { expenseVal.minus(it) }!!
        } else {
            incomeVal = incomeVal.minus(itemAtPos.price!!)
        }
        balanceVal = balanceVal.plus(itemAtPos.price!!)
        txtBalance.text =
            getString(R.string.balance_value, calculateBalance(incomeVal, expenseVal))
        expense.text = getString(R.string.balance_value, expenseVal)
        income.text = getString(R.string.balance_value, incomeVal)
        tempItemArray.remove(itemAtPos)
        if (tempItemArray.size == 0) dateModelArray.remove(tempDateArrayObj)

        saveIntoDb(expenseVal, incomeVal, balanceVal, dateModelArray)
        setProgressBar()
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