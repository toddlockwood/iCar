package sde.com.icar.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_car.view.*
import sde.com.icar.R
import sde.com.icar.model.car.Car


class CarsAdapter(private val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var carList: ArrayList<Car>? = null
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_car, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder){
            val model = carList!![position]
            holder.itemView.tvBrand.text = model.brand
            holder.itemView.tvModel.text = model.model
            holder.itemView.tvRegNum.text = model.registration
            holder.itemView.ivColor.setColorFilter(Color.parseColor(model.color));

            if (onClickListener != null) {
                onClickListener!!.onClick(position, model)
            }
        }
    }

    override fun getItemCount(): Int {
        if (carList == null){
            return 0
        }
        return carList!!.size
    }



    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setCars(cars: List<Car>?) {
        if (cars == null){
            carList = ArrayList()
            notifyDataSetChanged()
            return
        }
        val oldSize = if (carList != null) carList!!.size else 0
        val newSize = cars!!.size
        carList = ArrayList(cars)
        if (oldSize <= 0){
            notifyDataSetChanged()
        }else if (newSize > oldSize){
            notifyItemRangeInserted(oldSize, newSize - oldSize)
        }else{
            notifyDataSetChanged()
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, car: Car)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)


}