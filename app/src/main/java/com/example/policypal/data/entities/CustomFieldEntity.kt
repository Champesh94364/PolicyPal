package com.example.policypal.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "custom_fields",
    foreignKeys = [
        ForeignKey(
            entity = ProposalEntity::class,
            parentColumns = ["id"],
            childColumns = ["proposalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("proposalId")]
)
data class CustomFieldEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val proposalId: Long,

    // optional grouping (e.g., "Policyholder", "Nominee", "Bank")
    val section: String = "",

    val label: String = "",
    val value: String = ""
)