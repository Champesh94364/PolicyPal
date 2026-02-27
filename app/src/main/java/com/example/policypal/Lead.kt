package com.example.policypal

data class Lead(
    val id: String = System.currentTimeMillis().toString(),
    var name: String,
    var phone: String,
    var age: String,
    var city: String,
    var income: String,
    var status: String = "Active",
    val customFields: List<Pair<String, String>> = emptyList(),
    val checklist: List<Boolean> = emptyList()
)