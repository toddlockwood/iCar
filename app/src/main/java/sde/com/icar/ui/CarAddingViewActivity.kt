package sde.com.icar.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_car_adding_view.*
import sde.com.icar.R
import sde.com.icar.model.car.Car
import sde.com.icar.viewmodel.CarAddingViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CarAddingViewActivity : AppCompatActivity(), View.OnClickListener {

    private var mViewModel: CarAddingViewModel? = null
    private var mDateText: TextView? = null
    private var mInputRegNumText: EditText? = null
    private var mInputModelText: EditText? = null
    private var mInputBrandText: EditText? = null
    private lateinit var selectedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_adding_view)
        mViewModel = ViewModelProvider(this).get(CarAddingViewModel::class.java)
        init()
        setOnClickListeners()

    }

    private fun init() {
        mDateText = findViewById(R.id.txtSelectedDate)
        mInputBrandText = findViewById(R.id.etBrand)
        mInputModelText = findViewById(R.id.etModel)
        mInputRegNumText = findViewById(R.id.etReg)
    }

    private fun setOnClickListeners() {
        btnDatePicker.setOnClickListener(this)
        btnSaveCar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnDatePicker -> {
                clickDatePicker(v)
            }
            R.id.btnSaveCar -> {
                onCreateCarBtnClick()
            }
        }
    }

    private fun onCreateCarBtnClick() {
        var tmpCar: Car = Car()
        tmpCar.brand = mInputBrandText!!.text.toString()
        if (tmpCar.brand.isNullOrBlank()) {
            mInputBrandText!!.error = "You cannot create blank brand"
            return
        }
        tmpCar.model = mInputModelText!!.text.toString()
        if (tmpCar.model.isNullOrBlank()) {
            mInputModelText!!.error = "You cannot create blank model"
            return
        }
        tmpCar.year = getDateFromText(mDateText!!.text.toString())
        if (tmpCar.year.isNullOrBlank()) {
            mDateText!!.text = "You have to choose date"
            return
        }
        tmpCar.registration = mInputRegNumText!!.text.toString()
        if (tmpCar.registration.isNullOrBlank()) {
            mInputRegNumText!!.error = "You cannot create blank registration"
            return
        }

        mViewModel!!.setCar(tmpCar)
        mViewModel!!.createCar()
    }

    private fun getDateFromText(date: String): String? {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val formatterOut = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        var theDate: String = ""

        try {
            val date = formatter.parse(date)
            theDate = formatterOut.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return theDate
    }


    private fun clickDatePicker(view: View) {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            txtSelectedDate.text = selectedDate
        }, year, month, dayOfMonth)
        dpd.datePicker.maxDate = Date().time
        dpd.show()
    }

    override fun onStart() {
        super.onStart()
        observeViewModel();
    }

    fun observeViewModel() {
        mViewModel!!.carAddedB?.observe(this, Observer<Boolean?> { isAdded: Boolean? -> onCarAdded(isAdded) })

    }


    private fun onCarAdded(isAdded: Boolean?) {
        if (isAdded!!)
            finish()
    }

}