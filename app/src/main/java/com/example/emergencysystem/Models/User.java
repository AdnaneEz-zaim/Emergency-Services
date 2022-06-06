package com.example.emergencysystem.Models;

public class User {
    String id;
    String prenom;
    String nom;
    Role role;
    String email;

    public User() {
    }

    public User(String id, String prenom, String nom, String email, Role role) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.role = role;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
