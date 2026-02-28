package com.example.policypal.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // ---------- Enums ----------
    @TypeConverter fun roleToString(v: PersonRole?): String? = v?.name
    @TypeConverter fun stringToRole(v: String?): PersonRole? = v?.let { PersonRole.valueOf(it) }

    @TypeConverter fun addrTypeToString(v: AddressType?): String? = v?.name
    @TypeConverter fun stringToAddrType(v: String?): AddressType? = v?.let { AddressType.valueOf(it) }

    @TypeConverter fun genderToString(v: Gender?): String? = v?.name
    @TypeConverter fun stringToGender(v: String?): Gender? = v?.let { Gender.valueOf(it) }

    @TypeConverter fun proposalStatusToString(v: ProposalStatus?): String? = v?.name
    @TypeConverter fun stringToProposalStatus(v: String?): ProposalStatus? = v?.let { ProposalStatus.valueOf(it) }

    // ---------- List<Boolean> checklist ----------
    @TypeConverter
    fun booleanListToJson(list: List<Boolean>?): String? = gson.toJson(list)

    @TypeConverter
    fun jsonToBooleanList(json: String?): List<Boolean> {
        if (json.isNullOrBlank()) return emptyList()
        val type = object : TypeToken<List<Boolean>>() {}.type
        return gson.fromJson(json, type)
    }

    // ---------- List<HealthCondition> ----------
    @TypeConverter
    fun healthListToJson(list: List<HealthCondition>?): String? = gson.toJson(list)

    @TypeConverter
    fun jsonToHealthList(json: String?): List<HealthCondition> {
        if (json.isNullOrBlank()) return emptyList()
        val type = object : TypeToken<List<HealthCondition>>() {}.type
        return gson.fromJson(json, type)
    }
}