package com.example.investmentsapp

import io.finnhub.api.models.CompanyProfile2
import io.finnhub.api.models.Quote

class StocksInfo(var counter:Int, var item:CompanyProfile2, var price:Quote) {
}