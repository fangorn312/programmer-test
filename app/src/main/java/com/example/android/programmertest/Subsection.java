package com.example.android.programmertest;

public class Subsection {
    private int Id;

    public Subsection(int id, String name) {
        Id = id;
        Name = name;
    }

    private String Name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
