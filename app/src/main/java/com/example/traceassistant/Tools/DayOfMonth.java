/**
 * 根据月份获取该月的总天数的工具类
 */
package com.example.traceassistant.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DayOfMonth {

    private DayOfMonth(){}

    /**
     * 根据月份获取该月的总天数
     * @param date 格式（yyyy-mm）例如"2020-08"
     * @return days
     */
    public static int getDayNumber(String date){
        date = date+"-01";
        System.out.println(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
