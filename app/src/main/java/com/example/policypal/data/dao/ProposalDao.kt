package com.example.policypal.data.dao

import androidx.room.*
import com.example.policypal.data.entities.*
import com.example.policypal.data.relations.ProposalFullData
import kotlinx.coroutines.flow.Flow

@Dao
interface ProposalDao {

    // ---------- Proposal ----------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProposal(p: ProposalEntity): Long

    @Update suspend fun updateProposal(p: ProposalEntity)

    @Query("SELECT * FROM proposals ORDER BY updatedAt DESC")
    fun observeAllProposals(): Flow<List<ProposalEntity>>

    @Transaction
    @Query("SELECT * FROM proposals WHERE id = :proposalId LIMIT 1")
    fun observeProposalFull(proposalId: Long): Flow<ProposalFullData?>

    @Query("DELETE FROM proposals WHERE id = :proposalId")
    suspend fun deleteProposal(proposalId: Long)

    // ---------- Persons ----------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(p: PersonEntity): Long

    @Update suspend fun updatePerson(p: PersonEntity)

    // ---------- Links ----------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLink(link: ProposalPersonLinkEntity)

    @Query("DELETE FROM proposal_person_links WHERE proposalId = :proposalId")
    suspend fun deleteAllLinksForProposal(proposalId: Long)

    // ---------- Address / Occupation / Bank / Health ----------
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAddress(a: AddressEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertOccupation(o: OccupationEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertBank(b: BankDetailsEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertHealth(h: HealthEntity): Long

    @Query("DELETE FROM addresses WHERE personId = :personId") suspend fun deleteAddresses(personId: Long)
    @Query("DELETE FROM occupations WHERE personId = :personId") suspend fun deleteOccupations(personId: Long)
    @Query("DELETE FROM bank_details WHERE personId = :personId") suspend fun deleteBanks(personId: Long)
    @Query("DELETE FROM health WHERE personId = :personId") suspend fun deleteHealth(personId: Long)

    // ---------- Checklist ----------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChecklist(c: ChecklistEntity): Long

    @Query("DELETE FROM checklists WHERE proposalId = :proposalId")
    suspend fun deleteChecklist(proposalId: Long)

    // ---------- Custom Fields ----------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomField(cf: CustomFieldEntity): Long

    @Query("DELETE FROM custom_fields WHERE proposalId = :proposalId")
    suspend fun deleteCustomFields(proposalId: Long)

    // ---------- Atomic “replace all” helper ----------
    @Transaction
    suspend fun replaceCustomFields(proposalId: Long, newFields: List<CustomFieldEntity>) {
        deleteCustomFields(proposalId)
        newFields.forEach { insertCustomField(it.copy(proposalId = proposalId)) }
    }
}