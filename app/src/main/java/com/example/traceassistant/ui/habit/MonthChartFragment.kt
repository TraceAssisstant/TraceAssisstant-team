package com.example.traceassistant.ui.habit

import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.traceassistant.R
import com.example.traceassistant.logic.Repository
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AALabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AATooltip
import com.github.aachartmodel.aainfographics.aatools.AAColor
import kotlinx.android.synthetic.main.fragment_daily_chart.*
import kotlinx.android.synthetic.main.fragment_daily_chart.focusTime
import kotlinx.android.synthetic.main.fragment_daily_chart.thisFocusTime
import kotlinx.android.synthetic.main.fragment_month_chart.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MonthChartFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_month_chart, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //显示本次专注时间
        val thisHour= requireActivity().intent.getStringExtra("hour")
        val thisMinute=requireActivity().intent.getStringExtra("minute")
        val thisSecond=requireActivity().intent.getStringExtra("second")
        Log.d("Hour&Minute&Second","${thisHour}时${thisMinute}分${thisSecond}秒")
        if (!thisHour.equals("00")){
            thisFocusTime.text="本次专注时间${thisHour}时${thisMinute}分${thisSecond}秒"
        }else if (!thisMinute.equals("00")){
            thisFocusTime.text="本次专注时间${thisMinute}分${thisSecond}秒"
        }else{
            thisFocusTime.text="本次专注时间${thisSecond}秒"
        }
        thisFocusTime.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

        //显示本月总专注时间
        val myDateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM")
        val month=myDateTimeFormatter.format(LocalDateTime.now())
        Repository.initHabitDao()
        focusTime.text="本月专注总时间：${date_to_string(Repository.habitQueryByMouth(month.toString()).first)}"
        focusTime.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

        //选择要显示的月份专注曲线
        val c = Calendar.getInstance() //可以对每个时间域单独修改
        val onlyMonth = (c[Calendar.MONTH]+1).toString()
        Log.d("onlyMonth","${onlyMonth}")
        var position:Int
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.month_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            //设置默认选项为当月
            position=adapter.getPosition("${month_String(onlyMonth)}")
            spinner.setSelection(position)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                //Log.d("pos","${selectedItem}")
                //显示当日专注时间与中断专注时间对比折线图
                focus_month_view.aa_drawChartWithChartOptions(focus_month_chart(selectedItem))
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    /**
     * 将时间戳元素转换为字符串格式并返回
     */

    @RequiresApi(Build.VERSION_CODES.N)
    fun date_to_string(i:Long):String{
        if (i>=3600000){
            return "${SimpleDateFormat("HH时mm分ss秒").format(i)}"
        }else if (i>60000){
            return "${SimpleDateFormat("mm分ss秒").format(i)}"
        }else{
            return "${SimpleDateFormat("ss秒").format(i)}"
        }
    }

    /**
     * 将所选月与yyyy-mm格式日期对应
     */

    @RequiresApi(Build.VERSION_CODES.O)
    fun select_month(month:String):String{
        val myDateTimeFormatter2=DateTimeFormatter.ofPattern("yyyy")
        val year=myDateTimeFormatter2.format(LocalDateTime.now())
        Log.d("year","${year}")
        when(month){
            "一月"->return "${year}-01"
            "二月"->return "${year}-02"
            "三月"->return "${year}-03"
            "四月"->return "${year}-04"
            "五月"->return "${year}-05"
            "六月"->return "${year}-06"
            "七月"->return "${year}-07"
            "八月"->return "${year}-08"
            "九月"->return "${year}-09"
            "十月"->return "${year}-10"
            "十一月"->return "${year}-11"
            "十二月"->return "${year}-12"
            else->return "err"
        }
    }

    /**
     * 将所选月与m月格式日期对应
     */

    fun month_String(month:String)=when(month){
        "1"->"一月"
        "2"->"二月"
        "3"->"三月"
        "4"->"四月"
        "5"->"五月"
        "6"->"六月"
        "7"->"七月"
        "8"->"八月"
        "9"->"九月"
        "10"->"十月"
        "11"->"十一月"
        "12"->"十二月"
        else->"err"
    }

    /**
     * 获取专注时间与中断专注时间对比折线图x轴月份数组
     */

    @RequiresApi(Build.VERSION_CODES.O)
    fun month_day(month: String):List<String>{
        val list= mutableListOf<String>()
        for (i in 1..Repository.focusArrayQuery(select_month(month)).size){
            list.add("${i}")
        }
        return list
    }

    /**
     * 显示月度专注时间曲线图
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun focus_month_chart(month: String): AAOptions {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Line)//图形类型
            .title("月度专注时间与中断专注时间折线图")//图表主标题
            .dataLabelsEnabled(false)
            .colorsTheme(arrayOf("#04d69f", "#1e90ff"))
            .dataLabelsEnabled(false)
            .categories(month_day(month).toTypedArray())
            .series(arrayOf(
                AASeriesElement()
                .name("focus time")
                .lineWidth(5.0f)
                .fillOpacity(0.4f)
                .data(Repository.focusArrayQuery(select_month(month)) as Array<Any>),
                AASeriesElement()
                    .name("pause time")
                    .lineWidth(5.0f)
                    .fillOpacity(0.4f)
                    .data(Repository.pauseArrayQuery(select_month(month))as Array<Any>)
            ))

        val aaYAxisLabels = AALabels()
            .style(
                AAStyle()
                .fontSize(10f)
                .fontWeight(AAChartFontWeightType.Bold)
                .color(AAColor.Gray))
            .formatter("""
function () {
        var yValue = this.value;
        if (yValue == 0) {
            return "0";
        } else if (yValue == 600000) {
            return "10min";
        } else if (yValue == 1200000) {
            return "20min";
        } else if (yValue == 1800000) {
            return "30min";
        } else if (yValue == 2400000) {
            return "40min";
        }else if (yValue == 3000000) {
            return "50min";
        }else if (yValue == 3600000) {
            return "1h";
        }
    }
                """.trimIndent()
            )

        val aaTooltip = AATooltip()
            .useHTML(true)
            .borderColor("#FFD700")
            .style(
                AAStyle()
                .color("#4b2b7f")
                .fontSize(12)
            )
            .formatter(
                """
        function () {
      let wholeContentStr = '<br/>';
      let length = this.points.length;
      for(let i = 0; i < length; i++) {
        let yValue=this.points[i].y;
        let yDate = new Date(this.points[i].y);
        let prefixStr = '<span style=\"' + 'color:'+ this.points[i].color + '; font-size:13px\"' + '>◉ ';
        if(yValue>=3600000){
            wholeContentStr+=prefixStr + this.points[i].series.name + ': '+yDate.getHours()-8+'时'+yDate.getMinutes()+'分'+yDate.getSeconds()+'秒'+ '<br/>';
        }else if(yValue>=60000){
            wholeContentStr+=prefixStr + this.points[i].series.name + ': '+yDate.getMinutes()+'分'+yDate.getSeconds()+'秒'+ '<br/>';
        }else{
            wholeContentStr+=prefixStr + this.points[i].series.name + ': '+yDate.getSeconds()+'秒'+ '<br/>';
        }
      }
        return wholeContentStr;
    }
                """.trimIndent()
            )
        val aaOptions = aaChartModel.aa_toAAOptions()
        aaOptions.tooltip = aaTooltip
        aaOptions.yAxis!!
            .opposite(true)
            .tickWidth(2f)
            .lineWidth(1.5f)//Y轴轴线颜色
            .lineColor(AAColor.LightGray)//Y轴轴线颜色
            .gridLineWidth(0f)//Y轴网格线宽度
            .tickPositions(arrayOf(0, 600000, 1200000, 1800000, 2400000,3000000,3600000))
            .labels(aaYAxisLabels)
        return aaOptions
    }
}