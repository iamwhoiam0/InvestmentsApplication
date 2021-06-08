package com.example.investmentsapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.investmentsapp.db.DBHelper
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.CompanyProfile2
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private val symbolsStocks = listOf(
        "PRGO", "CAMP", "TUP", "DAKT", "ESRT", "EPD", "FDP", "NVO", "ORA", "PAYX"
    )     // массив тикеров акций
    private val listElements: MutableList<Item> = ArrayList()                                               // список, состоящий из данных акции

    private val apiClient = DefaultApi()    // инициализация API

    lateinit var listView: ListView
    lateinit var editText: EditText
    lateinit var adapter: StockAdapter
    // logo(фото), name(Apple inc), ticker (AAPL), currency (USD = $)

    lateinit var txtStocksButton: TextView
    lateinit var txtFavouriteButton: TextView

    var isFavourite = false     // проверка на
    var visibleCounter = 0      // счётчик для чередования вывода акций


    // Первый запуск
    private var firstStartApp: String = "first_start"
    private var firstStart = false
    var mPreferences: SharedPreferences? = null
    var mEditor: SharedPreferences.Editor? = null

    private var isCheck by Delegates.notNull<Boolean>()

    // Создаём базу данных
    var db = DBHelper(this)

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById<View>(R.id.listView) as ListView
        editText = findViewById<View>(R.id.txtSearch) as EditText
        txtStocksButton = findViewById<View>(R.id.txtStocks) as TextView        // кнопка Stocks
        txtFavouriteButton = findViewById<View>(R.id.txtFavourite) as TextView  // кнопка Favourite

        firstRunCheck()

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() == "") {
                    findViewById<EditText>(R.id.txtSearch).setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search_24, 0, 0, 0)
                    for(index in listElements.indices){
                        listElements[index].isShow = true
                    }
                    adapter = StockAdapter(this@MainActivity, listElements, isFavourite, visibleCounter)
                    listView.adapter = adapter
                } else {
                    // perform search
                    findViewById<EditText>(R.id.txtSearch).setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_backspace_24, 0, R.drawable.ic_close_24, 0)
                    searchItem(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        txtFavouriteButton.setOnClickListener(View.OnClickListener {
            txtStocksButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
            txtStocksButton.setTextColor(Color.GRAY)
            txtFavouriteButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28F)
            txtFavouriteButton.setTextColor(Color.BLACK)
            isFavourite = true
            visibleCounter = 0
            adapter = StockAdapter(this, listElements, isFavourite, visibleCounter)
            listView.adapter = adapter
        })
        txtStocksButton.setOnClickListener(View.OnClickListener {
            txtFavouriteButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
            txtFavouriteButton.setTextColor(Color.GRAY)
            txtStocksButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28F)
            txtStocksButton.setTextColor(Color.BLACK)
            isFavourite = false
            visibleCounter = 0
            adapter = StockAdapter(this, listElements, isFavourite, visibleCounter)
            listView.adapter = adapter
        })
    }

    @SuppressLint("CommitPrefEdits")
    private fun firstRunCheck(){
        mPreferences = getSharedPreferences("BasicSetting", Context.MODE_PRIVATE)
        mEditor = mPreferences?.edit()
        firstStart = true

        isCheck= mPreferences!!.getBoolean(firstStartApp, firstStart)

        if (isCheck) {          // При первом запуске приложения
            initList()
        }
        else {                // При повторных запусках приложения
            takeBd()
            adapter = StockAdapter(this, listElements, isFavourite, visibleCounter)
            listView.adapter = adapter
        }
    }

    override fun onPause() {
        super.onPause()
        if (isCheck){
            firstSaveBd()
        }else{
            otherSaveBd()
        }
        db.onClose()
        firstStart = false;
        mEditor?.putBoolean(firstStartApp, firstStart);
        mEditor?.apply()
    }

    private fun firstSaveBd(){
        for (index in listElements.indices){
            db.insertUserData(listElements[index].logo, listElements[index].ticker, listElements[index].companyName, listElements[index].price, listElements[index].delta, listElements[index].currency, listElements[index].fav, listElements[index].isShow)
        }
    }

    private fun otherSaveBd(){
        for (index in listElements.indices){
            db.updateUserData(listElements[index].logo, listElements[index].ticker, listElements[index].companyName, listElements[index].price, listElements[index].delta, listElements[index].currency, listElements[index].fav, listElements[index].isShow)
        }
    }
    private fun takeBd(){
        val res: Cursor = db.getData()

        while(res.moveToNext()){
            listElements.add(
                    res.position,
                    Item(
                            logo = res.getString(1),
                            ticker = res.getString(2),
                            companyName = res.getString(3),
                            price = res.getString(4),
                            delta = res.getString(5),
                            currency = res.getString(5),
                            fav = res.getInt(7) == 1,
                            isShow = res.getInt(8) == 1
                    )
            )
        }
    }

    private fun initList(){
        val values = Observable.create { subscriber: ObservableEmitter<StocksInfo> ->
            ApiClient.apiKey["token"] = "c0mn2d748v6tkq136ncg"
            for (ticker in symbolsStocks.indices) {
                val newAny = StocksInfo(
                        counter = ticker,
                        item = apiClient.companyProfile2(symbolsStocks[ticker], null, null),
                        price = apiClient.quote(symbolsStocks[ticker])
                )
                subscriber.onNext(newAny)
            }
            subscriber.onComplete()
        }

        values
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            val company: StocksInfo = it
                            val refactorDelta:String = refactoringCompanyInfo(company)
                            listElements.add(
                                    company.counter,
                                    Item(
                                            logo = company.item.logo,
                                            ticker = company.item.ticker,
                                            companyName = company.item.name,
                                            price = "$" + company.price.c,
                                            delta = refactorDelta,
                                            currency = company.item.currency,
                                            fav = false,
                                            isShow = true
                                    )
                            )
                            Toast.makeText(this,"Жди, сучка!", Toast.LENGTH_SHORT).show()
                        }, {
                            e: Throwable -> Log.e(TAG, "Error: $e")
                        },{
                            adapter = StockAdapter(this, listElements, isFavourite, visibleCounter)
                            listView.adapter = adapter
                        }
                )
    }

    private fun refactoringCompanyInfo(company: StocksInfo):String{
        var newDelta:String = if(company.price.c?.minus(company.price.pc!!)!! < 0){
            "-$" + String.format("%.2f",(abs(company.price.c?.minus(company.price.pc!!)!!)))
        }else{
            "+$" + String.format("%.2f",(abs(company.price.c?.minus(company.price.pc!!)!!)))
        }

        newDelta += " (" + String.format("%.2f", (company.price.c?.minus(company.price.pc!!))?.div(company.price.c!!)?.let { abs(it).times(100) }) + "%)"
        return newDelta
    }

    fun searchItem(textToSearch: String?) {
        val searchLower = textToSearch!!.toLowerCase(Locale.ROOT)

        for(index in listElements.indices){
            val nameLower = listElements[index].companyName?.toLowerCase(Locale.ROOT)
            val tickerLower = listElements[index].ticker?.toLowerCase(Locale.ROOT)
            if (nameLower?.length!! < searchLower.length && tickerLower?.length!! < searchLower.length){
                continue
            }
            listElements[index].isShow = nameLower.substring(0, searchLower.length) == searchLower || tickerLower?.substring(0, searchLower.length) == searchLower

            //listElements[index].isShow = nameLower.substring(0, searchLower.length) == searchLower
        }
        adapter.notifyDataSetChanged()
    }
}