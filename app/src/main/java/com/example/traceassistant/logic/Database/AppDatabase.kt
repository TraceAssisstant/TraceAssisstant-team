package com.example.traceassistant.logic.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.traceassistant.logic.Dao.SignNatureDao
import com.example.traceassistant.logic.Entity.SignNature

@Database(version = 1, entities = [SignNature::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun SignNatureDao(): SignNatureDao
    companion object {
        private var instance: AppDatabase? = null
        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "sign_nature")
                .build().apply {
                    instance = this
                }
        }
    }
}