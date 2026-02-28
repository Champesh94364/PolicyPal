package com.example.policypal.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.policypal.data.Gender

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val fullName: String = "",
    val dobMillis: Long? = null,      // store DOB as millis (easy to calculate age)
    val ageYears: Int? = null,        // optional cached age (can be computed)
    val gender: Gender? = null,

    val phone: String = "",
    val email: String = "",

    // optional KYC fields
    val aadhaar: String = "",
    val pan: String = ""
)