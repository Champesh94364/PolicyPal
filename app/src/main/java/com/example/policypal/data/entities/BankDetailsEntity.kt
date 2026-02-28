package com.example.policypal.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bank_details",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("personId")]
)
data class BankDetailsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val personId: Long,

    val bankName: String = "",
    val accountNumber: String = "",
    val ifsc: String = "",
    val branch: String = ""
)