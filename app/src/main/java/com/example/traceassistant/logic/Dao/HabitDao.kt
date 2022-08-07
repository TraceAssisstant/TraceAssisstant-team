package com.example.traceassistant.logic.Dao

import androidx.room.*
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Entity.Habit
@Dao
interface HabitDao {
    /**
     * 插入习惯养成记录,传入习惯对象实体
     */
    @Insert
    fun habitInsert(habit: Habit)

    /**
     * 查询习惯养成记录,返回习惯列表
     */
    @Query("select * from habit")
    fun  habitQuery():List<Habit>

    /**
     * 根据id删除习惯养成记录
     */
    @Query("delete from habit where id = :id")
    fun habitDeleteById(id:Int)

}