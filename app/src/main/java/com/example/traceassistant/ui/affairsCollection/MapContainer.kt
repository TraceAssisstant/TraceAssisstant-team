package com.example.traceassistant.ui.affairsCollection

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout

class MapContainer(context: Context): RelativeLayout(context) {

    constructor(context: Context,attrs: AttributeSet) : this(context) {
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            if (ev.action == MotionEvent.ACTION_UP){
                requestDisallowInterceptTouchEvent(false)
            }else{
                requestDisallowInterceptTouchEvent(true)
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}