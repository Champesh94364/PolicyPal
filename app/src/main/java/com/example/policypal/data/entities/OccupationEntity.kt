package com.example.policypal.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "occupations",
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
data class OccupationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val personId: Long,

    val occupationType: String = "",      // Student / Salaried / Self-employed / etc
    val employerName: String = "",        // optional
    val jobTitle: String = "",            // optional
    val monthlyIncome: String = ""
)