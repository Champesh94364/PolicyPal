package com.example.policypal.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.policypal.data.HealthCondition

@Entity(
    tableName = "health",
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
data class HealthEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val personId: Long,

    val hasHealthIssues: Boolean = false,

    // Stored via Gson TypeConverter
    val conditions: List<HealthCondition> = emptyList()
)