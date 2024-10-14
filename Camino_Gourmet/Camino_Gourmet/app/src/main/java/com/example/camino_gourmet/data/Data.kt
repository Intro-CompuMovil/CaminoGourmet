package com.example.camino_gourmet.data

class Data {
    companion object {
        //Variables globales de toda la APP
        val MY_PERMISSION_LOCATION_CODE = 100
        val RESTAURANT_LIST =ArrayList<Restaurant>()
        var RESTAURANT_ROUTE = ArrayList<Restaurant>()
        var latitude: Double? = null
        var longitude: Double? = null
        val RADIUS_OF_EARTH_KM = 6371
    }
}