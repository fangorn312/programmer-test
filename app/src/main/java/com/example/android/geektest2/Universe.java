package com.example.android.geektest2;

/**
 * Created by Fangorn on 22.12.2017.
 */

public class Universe {
    private static final String TABLE_NAME = "Universe";

    private int ID;
    private String mUniverse;

    public Universe(String universe){
        mUniverse=universe;
    }

    public Universe(int ID, String UNIVERSE) {
        this.ID = ID;
        this.mUniverse = UNIVERSE;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUniverse() {
        return mUniverse;
    }

    public void setUniverse(String universe) {
        this.mUniverse = universe;
    }
}
