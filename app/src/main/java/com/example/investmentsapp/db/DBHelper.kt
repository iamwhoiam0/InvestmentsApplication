package com.example.investmentsapp.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.investmentsapp.db.DbConstants


class DBHelper(context: Context?) : SQLiteOpenHelper(context, DbConstants.ReadConst.Entry.DATABASE_NAME, null, DbConstants.ReadConst.Entry.DATABASE_VERSION) {
    override fun onCreate(DB: SQLiteDatabase) {
        DB.execSQL(DbConstants.ReadConst.CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DbConstants.ReadConst.DELETE_ENTRIES)
        onCreate(db)
    }

    fun insertUserData(logo: String?, ticker: String?, companyName: String?, price: String?, delta: String?, currency: String?, favourite: Boolean?, is_show:Boolean?): Boolean {
        var fav = 0
        var isShow = 0
        if (favourite!!)
            fav = 1
        if (is_show!!)
            isShow = 1
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_LOGO, logo)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_TICKER, ticker)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_COMPANY_NAME, companyName)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_PRICE, price)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_DELTA, delta)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_CURRENCY, currency)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_FAVOURITE, fav)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_IS_SHOW, isShow)
        val result = db.insert(DbConstants.ReadConst.Entry.TABLE_NAME, null, contentValues)
        return result != -1L
    }

    @SuppressLint("Recycle")
    fun updateUserData(logo: String?, ticker: String?, companyName: String?, price: String?, delta: String?, currency: String?, favourite: Boolean?, is_show:Boolean?): Boolean {
        var fav: Int = 0
        var isShow: Int = 0
        if (favourite!!)
            fav = 1
        if (is_show!!)
            isShow = 1
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_LOGO, logo)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_TICKER, ticker)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_COMPANY_NAME, companyName)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_PRICE, price)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_DELTA, delta)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_CURRENCY, currency)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_FAVOURITE, fav)
        contentValues.put(DbConstants.ReadConst.Entry.COLUMN_IS_SHOW, isShow)
        val cursor: Cursor = db.rawQuery("Select * from " + DbConstants.ReadConst.Entry.TABLE_NAME + " where "+ DbConstants.ReadConst.Entry.COLUMN_TICKER + " = ?", arrayOf(ticker))
        return if (cursor.count > 0) {
            val result = db.update(DbConstants.ReadConst.Entry.TABLE_NAME, contentValues, DbConstants.ReadConst.Entry.COLUMN_TICKER + "=?", arrayOf(ticker)).toLong()
            result != -1L
        } else {
            false
        }
    }

    @SuppressLint("Recycle")
    fun deleteData(name: String): Boolean {
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery("Select * from " + DbConstants.ReadConst.Entry.TABLE_NAME + " where " + DbConstants.ReadConst.Entry.COLUMN_TICKER + " = ?", arrayOf(name))
        return if (cursor.count > 0) {
            val result = db.delete(DbConstants.ReadConst.Entry.TABLE_NAME, DbConstants.ReadConst.Entry.COLUMN_TICKER + "=?", arrayOf(name)).toLong()
            result != -1L
        } else {
            false
        }
    }

    fun getData(): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("Select * from " + DbConstants.ReadConst.Entry.TABLE_NAME, null)
    }
    fun onClose(){
        val db = this.writableDatabase
        db.close()
    }
}