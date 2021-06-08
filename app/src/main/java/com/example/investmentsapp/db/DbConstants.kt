package com.example.investmentsapp.db

import android.provider.BaseColumns

class DbConstants {
    object ReadConst {
        // Table contents are grouped together in an anonymous object.
        object Entry : BaseColumns {
            const val TABLE_NAME = "StocksBD"
            const val COLUMN_LOGO = "logo"
            const val COLUMN_TICKER = "ticker"
            const val COLUMN_COMPANY_NAME= "company"
            const val COLUMN_PRICE= "price"
            const val COLUMN_DELTA= "delta"
            const val COLUMN_CURRENCY= "currency"
            const val COLUMN_FAVOURITE= "favourite"
            const val COLUMN_IS_SHOW= "isShow"

            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "Stocks.db"
        }
        const val CREATE_ENTRIES =
                "CREATE TABLE ${Entry.TABLE_NAME} (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                        "${Entry.COLUMN_LOGO} TEXT," +
                        "${Entry.COLUMN_TICKER} TEXT," +
                        "${Entry.COLUMN_COMPANY_NAME} TEXT," +
                        "${Entry.COLUMN_PRICE} TEXT," +
                        "${Entry.COLUMN_DELTA} TEXT," +
                        "${Entry.COLUMN_CURRENCY} TEXT," +
                        "${Entry.COLUMN_FAVOURITE} INTEGER," +
                        "${Entry.COLUMN_IS_SHOW} INTEGER);"

        const val DELETE_ENTRIES = "DROP TABLE IF EXISTS ${Entry.TABLE_NAME}"
    }
}