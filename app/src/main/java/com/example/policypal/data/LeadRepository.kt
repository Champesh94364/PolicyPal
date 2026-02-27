package com.example.policypal.data

class LeadRepository(private val leadDao: LeadDao) {

    suspend fun insertLead(lead: LeadEntity) = leadDao.insertLead(lead)

    suspend fun updateLead(lead: LeadEntity) = leadDao.updateLead(lead)

    suspend fun deleteLead(lead: LeadEntity) = leadDao.deleteLead(lead)

    fun getAllLeads() = leadDao.getAllLeads()
}