package com.example.traceassistant.ui.habit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.traceassistant.R
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AALabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aatools.AAColor

class ShowDrawingView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_drawing_view)

        fun focus_pause_chart(): AAOptions {
            val aaChartModel = AAChartModel()
                .chartType(AAChartType.Bar)//图形类型
                .title("该月专注时间与中断专注时间")//图表主标题
                .subtitle("")//图表副标题
                .dataLabelsEnabled(false)
                .colorsTheme(arrayOf("#04d69f", "#1e90ff"))
                .dataLabelsEnabled(false)
                .categories(arrayOf(
                    "10-01", "10-02", "10-03", "10-04", "10-05", "10-06", "10-07", "10-08", "10-09", "10-10",
                    "10-11", "10-12", "10-13", "10-14", "10-15", "10-16"))
                .series(arrayOf(AASeriesElement()
                    .name("focus time")
                    .lineWidth(5.0f)
                    .fillOpacity(0.4f)
                    .data(arrayOf(2290000.9, 771000.5, 1106000.4, 1129000.2, 664400.0, 1176000.0, 883500.6, 1480000.5, 881600.4, 669400.1, 779500.6, 995400.4)),
                    AASeriesElement()
                        .name("pause time")
                        .lineWidth(5.0f)
                        .fillOpacity(0.4f)
                        .data(arrayOf(2239000.9, 471000.5, 1006000.4, 1290000.2, 564400.0, 517600.0, 483500.6, 1248000.5, 2816000.4, 694000.1, 679500.6, 895400.4))
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

        val focus_pause_view = findViewById<AAChartView>(R.id.focus_pause_view)
        focus_pause_view.aa_drawChartWithChartOptions(focus_pause_chart())

    }

}