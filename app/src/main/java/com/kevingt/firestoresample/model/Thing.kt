package com.kevingt.firestoresample.model

data class Thing(
    var id: String = "",
    val content: String = "",
    val type: TYPE = TYPE.NORMAL,
    val done: Boolean = false,
    var timestamp: Long = System.currentTimeMillis()
) {
    enum class TYPE {
        NORMAL,
        FOOD,
        MOVIE,
        TRAVEL
    }
}