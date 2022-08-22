package com.example.traceassistant.ui.habit

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityHabitViewBinding
import com.example.traceassistant.databinding.ActivityShowDrawingViewBinding
import com.example.traceassistant.logic.Repository
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AALabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aatools.AAColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ShowDrawingView : AppCompatActivity() {

    lateinit var binding: ActivityShowDrawingViewBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_drawing_view)
        binding=ActivityShowDrawingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val focus_pause_view = binding.focusPauseView
        val focus_month_view=binding.focusMonthView

        //显示本月总专注时间
        val myDateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM")
        val month=myDateTimeFormatter.format(LocalDateTime.now())
        Repository.initHabitDao()
        binding.focusTime.text="本月专注总时间：${Repository.habitQueryByMouth(month.toString()).first}"

        //显示当日专注时间与中断专注时间对比柱状图
        val myDateTimeFormatter1=DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val day=myDateTimeFormatter1.format(LocalDateTime.now())
        focus_pause_view.aa_drawChartWithChartOptions(focus_pause_chart(day))

        //选择要显示的月份专注曲线
        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.month_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
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
     * 显示当日专注时间与中断专注时间对比折线图
     */
    fun focus_pause_chart(day:String): AAOptions{
        Repository.initHabitDao()
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Bar)
            .title("当日专注时间与中断专注时间对比柱状图")
            .backgroundColor("#4b2b7f")
            .dataLabelsEnabled(false)
            .xAxisLabelsEnabled(false)
            .series(arrayOf(
                AASeriesElement()
                    .name("focus time")
                    .data(arrayOf(Repository.habitQueryByDate(day).first)),
                AASeriesElement()
                    .name("pause time")
                    .data(arrayOf(Repository.habitQueryByDate(day).second))
            )
            )
        val aaYAxisLabels = AALabels()
            .style(AAStyle()
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

        val aaOptions = aaChartModel.aa_toAAOptions()
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
            .series(arrayOf(AASeriesElement()
                .name("focus time")
                .lineWidth(5.0f)
                .fillOpacity(0.4f)
                .data(Repository.focusArrayQuery(select_month(month))as Array<Any>),
                AASeriesElement()
                    .name("pause time")
                    .lineWidth(5.0f)
                    .fillOpacity(0.4f)
                    .data(Repository.pauseArrayQuery(select_month(month))as Array<Any>)
            ))

        val aaYAxisLabels = AALabels()
            .style(AAStyle()
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

        val aaOptions = aaChartModel.aa_toAAOptions()
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