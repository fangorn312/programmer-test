package com.example.android.programmertest;

/**
 * Created by Fangorn on 22.12.2017.
 */

public class Section {
    private static final String TABLE_NAME = "Section";

    private int id;
    private String name;

    public Section(String universe){
        name =universe;
    }

    public Section(int id, String UNIVERSE) {
        this.id = id;
        this.name = UNIVERSE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniverse() {
        return name;
    }

    public void setUniverse(String universe) {
        this.name = universe;
    }
}
