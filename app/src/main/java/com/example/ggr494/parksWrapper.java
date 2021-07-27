package com.example.ggr494;

import java.io.Serializable;
import java.util.ArrayList;

public class parksWrapper implements Serializable{

    private static final long serialVersionUID = 1L;
    private ArrayList<Park> parks;

    public parksWrapper(ArrayList<Park> parks) {
        this.parks = parks;
    }

    public ArrayList<Park> getParks() {
        return this.parks;
    }
}
