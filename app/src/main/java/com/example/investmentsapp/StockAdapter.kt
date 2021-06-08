package com.example.investmentsapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import com.squareup.picasso.Picasso

class StockAdapter(
    var mainActivity: MainActivity,
    var listElements: List<Item>,
    var isFavourite: Boolean,
    var counter:Int
) : BaseAdapter() {

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(mainActivity).inflate(R.layout.list_item, parent, false)
        val logo = view?.findViewById<ImageView>(R.id.imgLogo)
        val ticker = view?.findViewById<TextView>(R.id.txtTicker)
        val companyName = view?.findViewById<TextView>(R.id.txtCompanyName)
        val currentPrice = view?.findViewById<TextView>(R.id.txtCurrentPrice)
        val dayDelta = view?.findViewById<TextView>(R.id.txtDayDelta)
        val favourite = view?.findViewById<View>(R.id.imgFavourite) as AppCompatImageButton

        val red = Color.parseColor("#B22424")
        val green = Color.parseColor("#24B25D")

        val maxTextSize = 22  //максимальная длина отображения названия компании



        Picasso.get().load(listElements[position].logo).into(logo)

        if(listElements[position].delta?.get(0)  == '-') // Проверка на отрицательность значения
            dayDelta?.setTextColor(red)
        else
            dayDelta?.setTextColor(green)


        if(isFavourite){
            if(listElements[position].isShow!! && listElements[position].fav!!){

                if(counter % 2 == 0) {
                    view.setBackgroundResource(R.drawable.custom_item)
                }
                else {
                    view.setBackgroundResource(R.drawable.custom_item_chet)
                }
                counter++

                ticker?.text = listElements[position].ticker
                companyName?.text = listElements[position].companyName
                if (companyName?.text?.length!! > maxTextSize) {
                    companyName.text = companyName.text.substring(0, maxTextSize) + "...";
                }
                currentPrice?.text = listElements[position].price
                dayDelta?.text = listElements[position].delta
                if (listElements[position].fav!!)
                    favourite.setImageResource(R.drawable.path_true)
                else
                    favourite.setImageResource(R.drawable.path)// Рисуем звезду
                favourite.setOnClickListener(View.OnClickListener { // меняем изображение на кнопке
                    if (listElements[position].fav!!){
                        listElements[position].fav = false
                        favourite.setImageResource(R.drawable.path)
                    }
                    else{
                        listElements[position].fav = true
                        favourite.setImageResource(R.drawable.path_true)
                    }
                })
                return view
            }
        }else{
            if(listElements[position].isShow!!){

                if(counter % 2 == 0) {
                    view.setBackgroundResource(R.drawable.custom_item)
                }
                else {
                    view.setBackgroundResource(R.drawable.custom_item_chet)
                }

                counter++

                ticker?.text = listElements[position].ticker
                companyName?.text = listElements[position].companyName
                if (companyName?.text?.length!! > maxTextSize) {
                    companyName.text = companyName.text.substring(0, maxTextSize) + "...";
                }
                currentPrice?.text = listElements[position].price
                dayDelta?.text = listElements[position].delta

                if (listElements[position].fav!!) {
                    favourite.setImageResource(R.drawable.path_true)
                }
                else {
                    favourite.setImageResource(R.drawable.path)// Рисуем звезду
                }
                favourite.setOnClickListener(View.OnClickListener { // меняем изображение на кнопке
                    if (listElements[position].fav!!){
                        listElements[position].fav = false
                        favourite.setImageResource(R.drawable.path)
                    }
                    else{
                        listElements[position].fav = true
                        favourite.setImageResource(R.drawable.path_true)
                    }
                })
                return view
            }
        }

        return LayoutInflater.from(mainActivity).inflate(R.layout.null_item, null)
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listElements.size
    }

}