package com.example.traceassistant.ui.habit

import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyChartFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_daily_chart, container, false)
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

        //显示当日专注时间与中断专注时间对比柱状图
        val myDateTimeFormatter1=DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val day=myDateTimeFormatter1.format(LocalDateTime.now())
        focus_pause_view.aa_drawChartWithChartOptions(focus_pause_chart(day))
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
     * 显示当日专注时间与中断专注时间对比柱状图
     */

    @RequiresApi(Build.VERSION_CODES.N)
    fun focus_pause_chart(day:String): AAOptions {
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