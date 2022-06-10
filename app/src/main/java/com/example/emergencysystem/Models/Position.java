package com.example.emergencysystem.Models;

public class Position {
    double positionY,positionX;
    String id;

    public Position() {
    }

    public Position(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Position(double positionY, double positionX, String id) {
        this.positionY = positionY;
        this.positionX = positionX;
        this.id = id;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
