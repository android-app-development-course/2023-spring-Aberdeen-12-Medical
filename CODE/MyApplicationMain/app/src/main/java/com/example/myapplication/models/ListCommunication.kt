package com.example.myapplication.models

data class ListCommunication(
    var title: String,
    var author: String,
    var identity: String,
    var ID: Int,
    var time: String
) {
    constructor(title: String, identity: String, time: String, author: String) : this(
        title,
        author,
        identity,
        0,
        time
    )
}
