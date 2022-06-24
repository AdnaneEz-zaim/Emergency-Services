package com.example.emergencysystem.Models;

public class UrgenceModel {
    String id;
    Position position;
    String date;
    int niveauxDanger;
    String description;
    String positionName;
    String title;
    int nombreBlesses;
    String takeBy;
    String status;
    String device;

    public UrgenceModel() {
    }

    public UrgenceModel(Position position, String date, int niveauxDanger, String description, String positionName, String title, int nombreBlesses,String takeBy,String status,String device) {
        this.position = position;
        this.date = date;
        this.niveauxDanger = niveauxDanger;
        this.description = description;
        this.positionName = positionName;
        this.title = title;
        this.nombreBlesses = nombreBlesses;
        this.status = status;
        this.takeBy = takeBy;
        this.device = device;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public UrgenceModel(String id, Position position, String date, int niveauxDanger, String description, String positionName, String title, int nombreBlesses,String takeBy,String status,String device) {
        this.id = id;
        this.position = position;
        this.date = date;
        this.niveauxDanger = niveauxDanger;
        this.description = description;
        this.positionName = positionName;
        this.title = title;
        this.nombreBlesses = nombreBlesses;
        this.takeBy = takeBy;
        this.status = status;
        this.device = device;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getTakeBy() {
        return takeBy;
    }

    public void setTakeBy(String takeBy) {
        this.takeBy = takeBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getNombreBlesses() {
        return nombreBlesses;
    }

    public void setNombreBlesses(int nombreBlesses) {
        this.nombreBlesses = nombreBlesses;
    }
}
