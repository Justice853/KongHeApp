package com.example.kongheapp.db;

public class Phone {
    private String number;
    private String province;
    private String carrier;
    private String catName;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getcatName() {
        return catName;
    }

    public void setcatName(String catName) {
        this.catName = catName;
    }

    @Override
    public String toString() {
        return "PhoneData{" +
                "number='" + number + '\'' +
                ", province='" + province + '\'' +
                ", catName='" + catName + '\'' +
                ", carrier='" + carrier + '\'' +
                '}';
    }
}
