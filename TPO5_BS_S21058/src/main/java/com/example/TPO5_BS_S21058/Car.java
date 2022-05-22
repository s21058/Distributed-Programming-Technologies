package com.example.TPO5_BS_S21058;

import java.util.Date;

public class Car {
    String type;
    String mark;
    int yearOfProduction;
    int fuelConsumption;

    public Car(String type, String mark, int yearOfProduction, int fuelConsumption){
        this.type=type;
        this.mark=mark;
        this.yearOfProduction=yearOfProduction;
        this.fuelConsumption=fuelConsumption;

    }
    public Car(String type, String mark,int fuelConsumption){
        this.type=type;
        this.mark=mark;
        this.fuelConsumption=fuelConsumption;
    }


    @Override
    public String toString() {
        return "Car{" +
                "type='" + type + '\'' +
                ", mark='" + mark + '\'' +
                ", yearOfProduction=" + yearOfProduction +
                ", fuelConsumption=" + fuelConsumption +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

}
