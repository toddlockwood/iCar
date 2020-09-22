package sde.com.icar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import sde.com.icar.R
import sde.com.icar.databinding.ActivityCarDetailsBinding
import sde.com.icar.model.car.Car
import sde.com.icar.model.car.Person
import sde.com.icar.viewmodel.CarDetailsViewModel

class CarDetailsActivity : AppCompatActivity() {

    private var mViewModel: CarDetailsViewModel? = null
    private var extraCar: Car? = null

    private var haveCar = false;
    private var havePerson = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)
        receiveExtras()

        mViewModel = ViewModelProvider(this).get(CarDetailsViewModel::class.java)
        if (extraCar != null) {
            mViewModel!!.setCarId(extraCar!!._id)
            mViewModel!!.setPersonId(extraCar!!.ownerId)
            mViewModel!!.getCarById()
            mViewModel!!.getPersonById()
        }
    }

    private fun receiveExtras() {
        if (intent.hasExtra(CarListActivity.EXTRA_CAR_DETAILS)) {
            extraCar = intent.getSerializableExtra(CarListActivity.EXTRA_CAR_DETAILS) as Car
        }
    }

    override fun onStart() {
        super.onStart()
        observeViewModel();
    }

    private fun observeViewModel() {
        mViewModel?.car?.observe(this, Observer<Car?> { car: Car?
            ->
            onCarsChanged(car as Car?)
        })
        mViewModel?.person?.observe(this, Observer<Person?> { person: Person?
            ->
            onPersonChanged(person as Person?)
        })
    }

    private fun onPersonChanged(person: Person?) {
        havePerson = true
        if (havePerson && haveCar)
            binding()
    }


    private fun onCarsChanged(car: Car?) {
        haveCar = true
        if (havePerson && haveCar)
            binding()
    }

    private fun binding() {
        val activityDetailsBinding = DataBindingUtil.setContentView<ActivityCarDetailsBinding>(this, R.layout.activity_car_details)
        activityDetailsBinding.carDetailsViewModel = mViewModel
    }


}