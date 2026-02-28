package com.example.policypal.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.policypal.data.ProposalStatus

@Entity(tableName = "proposals")
data class ProposalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    val status: ProposalStatus = ProposalStatus.DRAFT,

    // Basic policy info (you can extend later)
    val planName: String = "",
    val sumAssured: String = "",
    val premium: String = "",
    val termYears: String = "",
    val pptYears: String = "",
    val mode: String = "Monthly",

    // Life assured age logic / minor handling
    val isPolicyholderMinor: Boolean = false,

    val notes: String = ""
)