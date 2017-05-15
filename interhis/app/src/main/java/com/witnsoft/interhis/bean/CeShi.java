package com.witnsoft.interhis.bean;

/**
 * Created by ${liyan} on 2017/5/15.
 */

public class CeShi {
    private String name;
    private String sex;
    private String content;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public CeShi(String name, String sex, String content, int age) {

        this.name = name;
        this.sex = sex;
        this.content = content;
        this.age = age;
    }
}
