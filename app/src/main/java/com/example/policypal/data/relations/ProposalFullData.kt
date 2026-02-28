package com.example.policypal.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.policypal.data.entities.*

data class ProposalFullData(
    @Embedded val proposal: ProposalEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "proposalId",
        entity = ProposalPersonLinkEntity::class
    )
    val peopleLinks: List<LinkWithPerson>,

    @Relation(
        parentColumn = "id",
        entityColumn = "proposalId"
    )
    val checklist: List<ChecklistEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "proposalId"
    )
    val customFields: List<CustomFieldEntity>
)