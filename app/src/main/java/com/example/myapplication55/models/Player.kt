package com.example.myapplication55.models

 class Player constructor(
    var lat: Double,
    val lng: Double,
    val score: Int,
    val name: String
    ) {
    class Builder(
        var lat: Double,
        var lng: Double,
        var score: Int,
        var name: String
    )
    {
        fun lat(lat: Double) = apply { this.lat = lat }
        fun lng(lng: Double) = apply { this.lng = lng }
        fun score(score: Int) = apply { this.score = score }
        fun name(name: String) = apply { this.name = name }
        fun build() = Player(lat, lng, score, name)

    }
}