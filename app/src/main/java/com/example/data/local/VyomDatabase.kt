package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AutomationRuleEntity
import com.example.data.model.ConversationEntity
import com.example.data.model.MemoryEntity
import com.example.data.model.MessageEntity

@Database(
    entities = [
        ConversationEntity::class,
        MessageEntity::class,
        MemoryEntity::class,
        AutomationRuleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VyomDatabase : RoomDatabase() {
    abstract fun vyomDao(): VyomDao

    companion object {
        @Volatile
        private var INSTANCE: VyomDatabase? = null

        fun getInstance(context: Context): VyomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VyomDatabase::class.java,
                    "vyom_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
