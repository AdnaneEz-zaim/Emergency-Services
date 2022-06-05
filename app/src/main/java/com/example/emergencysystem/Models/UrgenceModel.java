package com.example.emergencysystem.Models;

public class UrgenceModel {
    long id;
    String position;
    String date;
    int niveauxDanger;
    String description;
    String image;
    int nombreBlesses;

    public UrgenceModel() {
    }

    public UrgenceModel(long id, String position, String date, int niveauxDanger, String description, int nombreBlesses) {
        this.id = id;
        this.position = position;
        this.date = date;
        this.niveauxDanger = niveauxDanger;
        this.description = description;
        this.nombreBlesses = nombreBlesses;
    }

    public UrgenceModel(long id, String position, String date, int niveauxDanger, String description, String image, int nombreBlesses) {
        this.id = id;
        this.position = position;
        this.date = date;
        this.niveauxDanger = niveauxDanger;
        this.description = description;
        this.image = image;
        this.nombreBlesses = nombreBlesses;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNombreBlesses() {
        return nombreBlesses;
    }

    public void setNombreBlesses(int nombreBlesses) {
        this.nombreBlesses = nombreBlesses;
    }
}
