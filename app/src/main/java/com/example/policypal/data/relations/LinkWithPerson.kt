package com.example.policypal.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.policypal.data.entities.PersonEntity
import com.example.policypal.data.entities.ProposalPersonLinkEntity

data class LinkWithPerson(
    @Embedded val link: ProposalPersonLinkEntity,

    @Relation(
        parentColumn = "personId",
        entityColumn = "id"
    )
    val person: PersonEntity
)