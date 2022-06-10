package com.example.emergencysystem.Models;

public class EmergencyFirebaseModel {
    String id;
    String Device;
    String date;
    int niveauxDanger;
    int nombreBlesses;
    Position position;
    String positionName;
    String title;
    String description;

    public EmergencyFirebaseModel() {
    }

    public EmergencyFirebaseModel(String id, String device, String date, int niveauxDanger, int nombreBlesses, Position position, String positionName, String title, String description) {
        this.id = id;
        Device = device;
        this.date = date;
        this.niveauxDanger = niveauxDanger;
        this.nombreBlesses = nombreBlesses;
        this.position = position;
        this.positionName = positionName;
        this.title = title;
        this.description = description;
    }

    public EmergencyFirebaseModel(String device, String date, int niveauxDanger, int nombreBlesses, Position position, String positionName, String title, String description) {
        Device = device;
        this.date = date;
        this.niveauxDanger = niveauxDanger;
        this.nombreBlesses = nombreBlesses;
        this.position = position;
        this.positionName = positionName;
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String device) {
        Device = device;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNiveauxDanger() {
        return niveauxDanger;
    }

    public void setNiveauxDanger(int niveauxDanger) {
        this.niveauxDanger = niveauxDanger;
    }

    public int getNombreBlesses() {
        return nombreBlesses;
    }

    public void setNombreBlesses(int nombreBlesses) {
        this.nombreBlesses = nombreBlesses;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
