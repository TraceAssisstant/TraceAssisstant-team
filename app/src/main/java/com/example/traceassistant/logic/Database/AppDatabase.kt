package com.example.traceassistant.logic.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.traceassistant.logic.Dao.AffairFormDao
import com.example.traceassistant.logic.Dao.SignNatureDao
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Entity.SignNature

@Database(version = 2, entities = [SignNature::class, AffairForm::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun signNatureDao(): SignNatureDao
    abstract fun affairFormDao(): AffairFormDao

    companion object {

        private var instance: AppDatabase? = null
        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "AppDatabase")
                .fallbackToDestructiveMigration()
                .build().apply {
                    instance = this
                }
        }
    }
}
