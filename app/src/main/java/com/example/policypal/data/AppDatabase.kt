package com.example.policypal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.policypal.data.dao.ProposalDao
import com.example.policypal.data.entities.*

@Database(
    entities = [
        ProposalEntity::class,
        PersonEntity::class,
        AddressEntity::class,
        OccupationEntity::class,
        BankDetailsEntity::class,
        HealthEntity::class,
        ProposalPersonLinkEntity::class,
        ChecklistEntity::class,
        CustomFieldEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun proposalDao(): ProposalDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "policypal.db"
                )
                    .fallbackToDestructiveMigration() // since you're replacing fully
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}