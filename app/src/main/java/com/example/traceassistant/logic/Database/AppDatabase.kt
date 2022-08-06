package com.example.traceassistant.logic.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.traceassistant.logic.Dao.AffairFormDao
import com.example.traceassistant.logic.Dao.HabitDao
import com.example.traceassistant.logic.Dao.SignNatureDao
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Entity.Habit
import com.example.traceassistant.logic.Entity.SignNature

@Database(version = 6, entities = [SignNature::class, AffairForm::class,Habit::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun SignNatureDao(): SignNatureDao
    abstract fun AffairFormDao(): AffairFormDao
    abstract fun HabitDao(): HabitDao
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
                .allowMainThreadQueries()     //开放主线程数据库操作权限
                .build().apply {
                    instance = this
                }
        }
    }
}
