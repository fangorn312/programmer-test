package com.example.android.geektest2;

/**
 * Created by Fangorn on 22.12.2017.
 */

public class Universes {
    private static final String TABLE_NAME = "Universes";

    private int ID;
    private String UNIVERSE;

    public Universes(int ID, String UNIVERSE) {
        this.ID = ID;
        this.UNIVERSE = UNIVERSE;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUNIVERSE() {
        return UNIVERSE;
    }

    public void setUNIVERSE(String UNIVERSE) {
        this.UNIVERSE = UNIVERSE;
    }
}
