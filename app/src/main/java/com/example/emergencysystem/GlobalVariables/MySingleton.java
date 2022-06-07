package com.example.emergencysystem.GlobalVariables;

import com.example.emergencysystem.Models.UrgenceModel;

import java.util.ArrayList;

public class MySingleton {
    ArrayList<UrgenceModel> urgenceArrayList=new ArrayList<>();;
    // Getter/setter
    public ArrayList<UrgenceModel> getUrgenceArrayList() {
        return urgenceArrayList;
    }

    public void setUrgenceArrayList(ArrayList<UrgenceModel> urgenceArrayList) {
        this.urgenceArrayList = urgenceArrayList;
    }

    public void addUrgence(UrgenceModel urgence){
        this.urgenceArrayList.add(urgence);
    }

    public static void setInstance(MySingleton instance) {
        MySingleton.instance = instance;
    }

    private static MySingleton instance;


    public static MySingleton getInstance() {
        if (instance == null)
            instance = new MySingleton();
        return instance;
    }

    private MySingleton() { }
}
