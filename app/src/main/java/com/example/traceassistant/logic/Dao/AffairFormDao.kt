package com.example.traceassistant.logic.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
     *  根据日期返回当天的事务,按照时间戳排序
     */
    @Query("select * from affair_form where date = :tdate order by time")
    fun affairQueryByDate(tdate:String):List<AffairForm>

    /**
     * 根据日期返回所有事务的列表（等级高低顺序）
     */
    @Query("select * from affair_form where date = :date order by level")
    fun affairQueryByLevel(date:String):List<AffairForm>

    /**
     * 返回所有未完成事务的列表（等级高低顺序）
     */
    @Query("select * from affair_form where state = 1 order by level")
    fun unFinishedAffairQuery():List<AffairForm>

    /**
     * 返回所有当前时间之后未完成事务的列表（等级高低顺序）
     */
    @Query("select * from affair_form where state = 1 and time > :time order by level")
    fun unFinishedAffairQueryByTime(time:Long):List<AffairForm>

    /**
     * 根据id删除事务
     */
    @Query("delete from affair_form where id= :tid")
    fun affairDeleteById(tid:Long)

    /**
     * 删除所有事务
     */
    @Query("delete from affair_form")
    fun affairDeleteAll()

    /**
     * 更新事务信息
     */
    @Update
    fun affairUpdate(affairForm: AffairForm)

    /**
     * 根据id更新事务状态state
     */
    @Query("update affair_form set state = :state where id = :id")
    fun affairStateUpdate(id:Int,state:Int)

    /**
     * 返回某个时间点后的事务
     */
    @Query("select * from affair_form where time > :time order by time")
    fun affairQueryFromTime(time:Long):List<AffairForm>

    /**
     * 根据事务id删除某项事务
     */
    @Query("delete from affair_form where id = :id")
    fun deleteAffairById(id:Int)


}