package com.example.traceassistant.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.traceassistant.Tools.GlobalApplication
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.logic.Dao.AffairFormDao
import com.example.traceassistant.logic.Dao.SignNatureDao
import com.example.traceassistant.logic.Database.AppDatabase
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Entity.SignNature
import kotlinx.coroutines.Dispatchers
import kotlin.concurrent.thread

object Repository {

    lateinit var sndao: SignNatureDao
    lateinit var affairFormDao: AffairFormDao

    /**
     * 初始化数据库dao sndao
     */
    fun initSndao(){
        sndao = AppDatabase.getDatabase(GlobalApplication.context).SignNatureDao()
    }


    /**
     * 存储图文对象
     * @param signNature : 签名对象
     */

    fun insertSN(signNature: SignNature){
        try{
        sndao.insertSN(signNature)
        }catch (e:Exception){
            Log.w("插入错误(可忽略)",e)
        }
    }

    /**
     * 批量插入图文，以初始化
     * @param strList: MutableList<String> 签名组成的列表
     * @param imageIdList: MutableList<Int> 图片id组成的列表
     * @return null
     */
    fun batchInsertSN(strList: MutableList<String>,imageIdList: MutableList<Int>){
        val i = strList.size
        for (k in 1..i){
            val str = strList[k-1]
            val image = imageIdList[k-1]
            insertSN(SignNature(str,image))
        }
    }

    /**
     * 根据id获取图文类
     * @param id ：图文表记录的id
     */
    fun loadSNById(id:Int) = liveData(Dispatchers.IO){
        val result = try {
            Result.success(sndao.loadSNById(id))
        }catch (e: Exception){
            Result.failure<String>(e)
        }
        emit(result)
    }

    /**
     * 遍历数据库
     */
    fun SNList(){
        thread {
            for (sn in sndao.SNList()){
                Log.d("对sn图文的遍历:",sn.toString())
            }
        }
    }

    /**
     *返回数据库中图文记录条数
     * @return num :图文记录总条数
     */
    fun getSnNumber():Int{
        var num = 0;

        var list: List<SignNature> = sndao.SNList()
            for (sn in list) {
                Log.d("对sn图文的:", sn.toString())
                num = num + 1
            }

        return num;
    }

    /**
     *  初始化affair数据库操作对象
     */
    fun initAFDao(){
        affairFormDao = AppDatabase.getDatabase(GlobalApplication.context).affairFormDao()
    }

    /**
     * 插入事务数据
     * @param affairForm :事务对象
     */
    fun insertAffiar(affairForm: AffairForm){
        try{
            affairFormDao.affairInsert(affairForm)
        }catch(e:Exception){
            Log.w("插入错误(可忽略)",e)
        }
    }

    /**
     * 根据事务时间 遍历事务数据
     * @return List<AffairForm> :根据时间排序的事务列表
     */
    fun getAffairList():List<AffairForm>{
        return affairFormDao.affairQueryByTime()
    }

}