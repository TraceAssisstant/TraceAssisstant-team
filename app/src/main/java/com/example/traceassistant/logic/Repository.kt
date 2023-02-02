package com.example.traceassistant.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.room.Query
import com.example.traceassistant.Tools.DayOfMonth
import com.example.traceassistant.Tools.GlobalApplication
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.logic.Dao.AffairFormDao
import com.example.traceassistant.logic.Dao.HabitDao
import com.example.traceassistant.logic.Dao.SignNatureDao
import com.example.traceassistant.logic.Database.AppDatabase
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Entity.Habit
import com.example.traceassistant.logic.Entity.SignNature
import kotlinx.coroutines.Dispatchers
import kotlin.concurrent.thread

object Repository {

    lateinit var sndao: SignNatureDao
    lateinit var affairFormDao: AffairFormDao
    lateinit var habitDao: HabitDao;

    /**
     * 初始化sndao**************************************************************************
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
     *  初始化affair数据库操作对象**********************************************************************
     */
    fun initAFDao(){
        affairFormDao = AppDatabase.getDatabase(GlobalApplication.context).AffairFormDao()
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

    /**
     * 根据日期返回当天的事务,按照时间戳排序
     * @param date:yyyy-mm-dd格式的日期字符串
     * @return List<AffairForm> 事务列表
     */
    fun getAffairListByDate(date:String ):List<AffairForm>{
        return affairFormDao.affairQueryByDate(date)
    }

    /**
     * 返回所有未完成事务的列表
     */
    fun unFinishedAffairQuery():List<AffairForm>{
        return affairFormDao.unFinishedAffairQuery()
    }

    /**
     *根据日期返回当天的事务，并按照重要等级排序
     * @param date:yyyy-mm-dd格式的日期字符串
     * @return List<AffairForm> 事务列表
     */
    fun getAffairListByDateLevel(date:String ):List<AffairForm>{
        return affairFormDao.affairQueryByLevel(date)
    }

    /**
     * 返回所有当前时间之后未完成事务的列表（等级高低顺序）
     */
    fun unFinishedAffairQueryByTime():List<AffairForm>{
        var time:Long  = System.currentTimeMillis()/1000
        return affairFormDao.unFinishedAffairQueryByTime(time)
    }

    /**
     * 根据事务id删除某项事务
     * @param id Int
     */
    fun deleteAffairById(id:Int){
        return affairFormDao.deleteAffairById(id)
    }

    /**
     * 删除所有事务(一般用来处理测试数据,慎用)
     */
    fun deleteAffairAll(){
        try{
            affairFormDao.affairDeleteAll()
        }catch(e:Exception){
            Log.w("删除错误,无记录(可忽略)",e)
        }
    }

    /**
     * 根据主键id来对事务信息进行更新
     * @param AffairForm
     */
    fun updateAffair(affairForm: AffairForm){
        if (!this::affairFormDao.isInitialized){
            initAFDao()
        }
        try{
            affairFormDao.affairUpdate(affairForm)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    /**
     * 根据主键id来对事务状态码进行更新
     * @param id:Int(事务id)
     * @param state:Int(状态码)
     */
    fun updateDateAffair(id: Int,state:Int){
        try{
            affairFormDao.affairStateUpdate(id,state)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    /**
     * 返回当前时间点后的事务
     */
    fun affairQueryFromTime():List<AffairForm>{
        var time:Long  = System.currentTimeMillis()/1000
        return affairFormDao.affairQueryFromTime(time)
    }

    /**
     * 初始化habitDao*********************************************************************************
     */
    fun initHabitDao(){
        habitDao = AppDatabase.getDatabase(GlobalApplication.context).HabitDao()
    }

    /**
     * 插入习惯养成信息
     * @param habit:Habit
     */
    fun habitInsert(habit: Habit){
        try{
            habitDao.habitInsert(habit);
        }catch (e:Exception){
            Log.w("插入错误(可忽略)",e)
        }
    }

    /**
     * 查询习惯养成记录,返回习惯列表
     */
    fun habitQuery():List<Habit>{
        return habitDao.habitQuery()
    }

    /**
     * 根据id删除习惯养成记录
     */
    fun habitDeleteById(id:Int){
        try{
            habitDao.habitDeleteById(id)}
        catch (e:Exception){
            Log.w("删除失败",e)
        }
    }

    /**
     * 根据日期返回 当天 专注的总时间和中断总时间
     * @param 格式为 yyyy-mm-dd(2019-12-08)格式的日期 字符串
     * @return 一个pair类型的返回值,包括两个变量:(focusTime,pauseTime) 专注时间,暂停时间
     */
    fun habitQueryByDate(date:String):Pair<Long,Long>{
        var habitList : List<Habit> = habitDao.habitQueryByDate(date)
        var focusTime : Long = 0
        var pauseTime : Long = 0
        for(habit in habitList){
            focusTime+=habit.endTime-habit.beginTime-habit.pauseTime
            pauseTime+=habit.pauseTime
        }
        return Pair(focusTime,pauseTime)
    }

    /**
     * 根据日期返回当前 月份 专注的总时间和中断总时间
     * @param 格式为 yyyy-mm(2020-09)格式的 月份 字符串
     * @return 一个pair类型的返回值,包括两个变量:(focusTime,pauseTime) 专注时间,暂停时间
     */
    fun habitQueryByMouth(date:String):Pair<Long,Long>{
        var habitList : List<Habit> = habitDao.habitQueryByMouth(date+"%")
        var focusTime : Long = 0
        var pauseTime : Long = 0
        for(habit in habitList){
            focusTime+=habit.endTime-habit.beginTime-habit.pauseTime
            pauseTime+=habit.pauseTime
        }
        return Pair(focusTime,pauseTime)
    }

    /**
     * 根据日期返回当前 年份 专注的总时间和中断总时间
     * @param 格式为 yyyy(2020)格式的 月份 字符串
     * @return 一个pair类型的返回值,包括两个变量:(focusTime,pauseTime) 专注时间,暂停时间
     */
    fun habitQueryByYear(date:String):Pair<Long,Long>{
        var habitList : List<Habit> = habitDao.habitQueryByYear(date+"%")
        var focusTime : Long = 0
        var pauseTime : Long = 0
        for(habit in habitList){
            focusTime+=habit.endTime-habit.beginTime-habit.pauseTime
            pauseTime+=habit.pauseTime
        }
        return Pair(focusTime,pauseTime)
    }

    /**
     * 根据月份返回当前 月份 各天专注的总时间数组
     * @param 格式为 yyyy-mm(2020-08)格式的 月份 字符串
     * @return 各天专注的总时间数组
     */
     fun focusArrayQuery(month:String):Array<Long>{
        var days:Int = DayOfMonth.getDayNumber(month)
        var focusArray = Array<Long>(days){0}
        for(i in 0..focusArray.size-1){
            var(focusTime,pauseTime) = habitQueryByDate(month+"-"+if ((i+1)<10) "0"+(i+1) else (i+1).toString())
            focusArray.set(i,focusTime)
//            Log.d("focus",(i+1).toString()+"->"+focusArray[i])
        }
        return focusArray
     }

    /**
     * 根据月份返回当前 月份 各天暂停的总时间数组
     * @param 格式为 yyyy-mm(2020-08)格式的 月份 字符串
     * @return 各天暂停的总时间数组
     */
    fun pauseArrayQuery(month:String):Array<Long>{
        var days:Int = DayOfMonth.getDayNumber(month)
        var pauseArray = Array<Long>(days){0}
        for(i in 0..pauseArray.size-1){
            var(focusTime,pauseTime) = habitQueryByDate(month+"-"+if ((i+1)<10) "0"+(i+1) else (i+1).toString())
            pauseArray.set(i,pauseTime)
//            Log.d("pause",(i+1).toString()+"->"+pauseArray[i])
        }
        return pauseArray
    }

}