package com.harvest;

public class Price {
    String fruit;
    String date;
    String price;
    public Price(String f,String d,String p){
        this.fruit = f;
        this.date = d;
        this.price = p;
    }
    public String getFruit() {
        return fruit;
    }
    public String getDate() {
        return (this.date);
    }
    public double getPrice() {
        return Double.parseDouble(price);
    }
    public String toString(){
        return fruit + " " + date + " " + price;
    }
}