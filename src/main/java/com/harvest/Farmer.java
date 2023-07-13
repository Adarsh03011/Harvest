package com.harvest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Farmer {
    String name;
    String  date;
    String fruit;
    String quantity;
    public Farmer(String name, String date, String fruit, String quantity){
        this.name = name;
        this.date = date;
        this.fruit = fruit;
        this.quantity = quantity;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    public String getFruit() {
        return this.fruit;
    }
    public int getMonth() throws ParseException {
        SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = spd.parse(date);
        return d1.getMonth();
    }
    public double  getQuantity() {
        return Double.parseDouble(quantity);
    }
    public String toString(){
        return name + " " + date + " " + fruit + " " + quantity;
    }
}