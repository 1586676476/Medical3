package com.witnsoft.interhis.bean;

/**
 * Created by ${liyan} on 2017/5/24.
 */

public class NumberBean {
    private String name;

    private int count;

    public NumberBean(int count) {
        this.count = count;
    }

    public NumberBean(String name){
        this.name=name;
    }

    public NumberBean() {
    }

    public NumberBean(String name, int count) {

        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NumberBean){
            NumberBean numberBean = (NumberBean) obj;
            return name.equals(numberBean.getName());
        }
        return super.equals(obj);
    }


}
