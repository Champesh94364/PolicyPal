package com.example.policypal.data

class LeadRepository(private val dao: LeadDao) {
    val leads = dao.observeAll()

    suspend fun insert(lead: LeadEntity) = dao.insert(lead)
    suspend fun update(lead: LeadEntity) = dao.update(lead)
    suspend fun delete(lead: LeadEntity) = dao.delete(lead)
}