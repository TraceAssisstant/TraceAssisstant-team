package com.example.traceassistant.Tools;

import java.io.Serializable;

public class SerialData implements Serializable {
    Object data;

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
