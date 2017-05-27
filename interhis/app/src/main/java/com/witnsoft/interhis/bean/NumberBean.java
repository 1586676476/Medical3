package com.witnsoft.interhis.bean;

/**
 * Created by ${liyan} on 2017/5/24.
 */

public class NumberBean {
    private String name;
    private int count;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public NumberBean(String name){
        this.name=name;
    }

    public NumberBean() {
    }

    public NumberBean(String name, int count,int position) {

        this.name = name;
        this.count = count;
        this.position=position;
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
