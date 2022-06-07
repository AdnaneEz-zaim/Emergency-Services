package com.example.emergencysystem.Models;

public class UrgenceModel {
    long id;
    Position position;
    String date;
    int niveauxDanger;
    String description;
    String positionName;
    String title;
    int nombreBlesses;
    public UrgenceModel() {
    }

    public UrgenceModel(Position position, String date, int niveauxDanger, String description, String positionName, String title, int nombreBlesses) {
        this.position = position;
        this.date = date;
        this.niveauxDanger = niveauxDanger;
        this.description = description;
        this.positionName = positionName;
        this.title = title;
        this.nombreBlesses = nombreBlesses;
    }

    public UrgenceModel(long id, Position position, String title , String date, int niveauxDanger, String description, int nombreBlesses) {
        this.id = id;
        this.position = position;
        this.date = date;
        this.niveauxDanger = niveauxDanger;
        this.title = title;
        this.description = description;
        this.nombreBlesses = nombreBlesses;
    }

    public UrgenceModel(long id, Position position, String title,String date, int niveauxDanger, String description, String positionName, int nombreBlesses) {
        this.id = id;
        this.position = position;
        this.date = date;
        this.title = title;
        this.niveauxDanger = niveauxDanger;
        this.description = description;
        this.positionName = positionName;
        this.nombreBlesses = nombreBlesses;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
