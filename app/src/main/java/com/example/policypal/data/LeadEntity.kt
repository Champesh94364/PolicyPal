package com.example.policypal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leads")
data class LeadEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val age: String = "",
    val city: String = "",
    val income: String = "",
    val status: String = "Active",
    val customFields: List<CustomField> = emptyList(),
    val checklist: List<Boolean> = emptyList()
)