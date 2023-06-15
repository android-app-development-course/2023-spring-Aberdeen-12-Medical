package com.example.myapplication.models

class ListAll(
    val account: String,
    val hospitalTitle: String,
    val hospitalAddress: String,
    val hospitalTelephone: String,
    val hospitalOfficial: String,
    val ID: Int
) {
    constructor(account: String, hospitalTitle: String, hospitalAddress: String) : this(
        account,
        hospitalTitle,
        hospitalAddress,
        "",
        "",
        0
    )
}
