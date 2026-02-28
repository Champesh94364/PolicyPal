package com.example.policypal.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.policypal.data.PersonRole

@Entity(
    tableName = "proposal_person_links",
    primaryKeys = ["proposalId", "personId", "role"],
    foreignKeys = [
        ForeignKey(
            entity = ProposalEntity::class,
            parentColumns = ["id"],
            childColumns = ["proposalId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("proposalId"), Index("personId")]
)
data class ProposalPersonLinkEntity(
    val proposalId: Long,
    val personId: Long,
    val role: PersonRole,

    // relationship info (especially for nominees / proposer)
    val relationToLifeAssured: String = "",

    // Only used if role == NOMINEE
    val sharePercent: Int? = null,

    // Helps when multiple nominees, mark primary nominee optionally
    val isPrimary: Boolean = false
)