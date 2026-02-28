package com.example.policypal.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "checklists",
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
data class ChecklistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val proposalId: Long,

    // Stored via TypeConverter List<Boolean>
    // order matches: Contacted, Needs Analysis, Docs Collected, Proposal Shared, Deal Closed
    val items: List<Boolean> = emptyList()
)