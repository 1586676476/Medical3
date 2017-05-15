package com.witnsoft.interhis.bean;

/**
 * Created by ${liyan} on 2017/5/15.
 */

public class Prescription {
    private String name;
    private int number;

    public Prescription(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
