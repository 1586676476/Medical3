package com.witnsoft.interhis.db;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by ${liyan} on 2017/5/16.
 */
@Table("prescription")
public class Prescription {
    //建表
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private String name;
    private int number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Prescription(int id, String name, int number) {

        this.id = id;
        this.name = name;
        this.number = number;
    }
}
