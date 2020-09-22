package sde.com.icar.ui


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import sde.com.icar.R
import sde.com.icar.model.car.Car
import sde.com.icar.ui.adapters.CarsAdapter
import sde.com.icar.viewmodel.CarListViewModel


class CarListActivity : AppCompatActivity(), View.OnClickListener {

    private var mViewModel: CarListViewModel? = null
    var mAdapter = CarsAdapter(this)
    var mCarList: RecyclerView? = null

    companion object {
        var EXTRA_CAR_DETAILS = "extra_car_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClickListeners()

        mViewModel = ViewModelProvider(this).get(CarListViewModel::class.java)

        setupRecyclerView()
        mViewModel!!.discoverCarList()
    }

    private fun setOnClickListeners() {
        btnAddCar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddCar -> {
                val intent = Intent(this@CarListActivity, CarAddingViewActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupRecyclerView() {
        mAdapter = CarsAdapter(this)
        mCarList = findViewById(R.id.rv_CarList)
        mCarList!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mCarList!!.setHasFixedSize(true)
        mCarList!!.adapter = mAdapter
        mAdapter.setOnClickListener(object : CarsAdapter.OnClickListener {
            override fun onClick(position: Int, car: Car) {
                val intent = Intent(this@CarListActivity, CarDetailsActivity::class.java)
                intent.putExtra(EXTRA_CAR_DETAILS, car)
                startActivity(intent)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        observeViewModel();
    }

    private fun observeViewModel() {
        mViewModel?.discoveredCars?.observe(this, Observer<List<Car?>?> { cars: List<Car?>?
            ->
            onCarsChanged(cars as List<Car>?)
        })
    }


    fun onCarsChanged(cars: List<Car>?) {
        mAdapter.setCars(cars)
    }
}

