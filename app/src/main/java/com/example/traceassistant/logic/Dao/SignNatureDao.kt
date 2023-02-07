/**
 * 图文表的DAO接口
 */
package com.example.traceassistant.logic.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.traceassistant.logic.Entity.SignNature
import javax.net.ssl.SNIHostName

@Dao
interface SignNatureDao {
    /**
     * 需要传入图文实体
     * 返回插入图文数据后的ID
     */
    @Insert
    fun insertSN(signNature: SignNature)

    /**
     *根据id(星期信息)返回图文实体类
     */
    @Query("select * from sign_nature where id = :tid ")
    fun loadSNById(tid: Int):SignNature

    /**
     * 删除SignNature
     */
    @Query("delete from sign_nature where id = :tid")
    fun deleteSNById(tid: Int):Int

    /**
     * 遍历SignNature列表
     */
    @Query("select * from sign_nature ")
    fun SNList() :List<SignNature>
}