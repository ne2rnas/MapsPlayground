package com.mapsplayground.view.map.utils

object CoordinateRegex {
    val latRegex =
        Regex("^([+\\-])?(?:$MAX_NORTH_COORDINATE(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))\$")
    val lonRegex =
        Regex("^([+\\-])?(?:$MAX_EAST_COORDINATE(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))\$")
}
