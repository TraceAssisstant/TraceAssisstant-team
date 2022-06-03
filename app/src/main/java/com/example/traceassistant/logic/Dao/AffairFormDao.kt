package com.example.traceassistant.logic.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.traceassistant.logic.Entity.AffairForm

@Dao
interface AffairFormDao {

    /**
     * 插入事务记录,需要传入事务对象
     */
    @Insert
    fun affairInsert(affairForm: AffairForm)

    /**
     * 根据id返回事务
     */
    @Query("select * from affair_form where id= :tid")
    fun affairQueryById( tid:Int):AffairForm

    /**
     * 根据时间先后顺序返回所有事务的列表
     */
    @Query("select * from affair_form order by time")
    fun affairQueryByTime():List<AffairForm>

    /**
     * 根据等级高低顺序返回所有事务的列表
     */
    @Query("select * from affair_form order by level")
    fun affairQueryByLevel():List<AffairForm>

    /**
     * 根据id删除事务
     */
    @Query("delete from affair_form where id= :tid")
    fun affairDeleteById(tid:Int)


}