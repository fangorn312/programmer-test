package com.example.android.geektest2;

public class Category {
    private int Id;

    public Category(int id, String name) {
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
