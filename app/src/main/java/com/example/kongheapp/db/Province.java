package com.example.kongheapp.db;

import org.litepal.crud.DataSupport;

public class Province extends DataSupport {
    private int id;//id
    private String provinceName;//省名
    private int provincceCode;//省代号

    public int getId() {
        return id;
    }

    public int getProvincceCode() {
        return provincceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvincceCode(int provincceCode) {
        this.provincceCode = provincceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
