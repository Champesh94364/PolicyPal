package com.example.policypal.data

enum class ProposalStatus { DRAFT, IN_PROGRESS, SUBMITTED, CLOSED }
enum class PersonRole { POLICYHOLDER, PROPOSER, NOMINEE }
enum class AddressType { PERMANENT, CORRESPONDENCE }
enum class Gender { MALE, FEMALE, OTHER }

// Stored in DB via Gson in HealthEntity.conditions
data class HealthCondition(
    val label: String,
    val details: String? = null
)