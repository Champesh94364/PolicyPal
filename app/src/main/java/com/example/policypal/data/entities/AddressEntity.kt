package com.example.policypal.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.policypal.data.AddressType

@Entity(
    tableName = "addresses",
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
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val personId: Long,
    val type: AddressType = AddressType.PERMANENT,

    val line1: String = "",
    val line2: String = "",
    val city: String = "",
    val state: String = "",
    val pincode: String = ""
)