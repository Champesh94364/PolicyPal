package com.example.policypal.data

import com.example.policypal.data.dao.ProposalDao
import com.example.policypal.data.entities.ProposalEntity
import com.example.policypal.data.relations.ProposalFullData
import kotlinx.coroutines.flow.Flow

class ProposalRepository(private val dao: ProposalDao) {

    fun observeAllProposals(): Flow<List<ProposalEntity>> = dao.observeAllProposals()

    fun observeProposalFull(id: Long): Flow<ProposalFullData?> = dao.observeProposalFull(id)

    suspend fun insertProposal(p: ProposalEntity): Long = dao.insertProposal(p)

    suspend fun updateProposal(p: ProposalEntity) = dao.updateProposal(p)

    suspend fun deleteProposal(id: Long) = dao.deleteProposal(id)
}