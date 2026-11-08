/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestionmission.Metier;

import java.time.LocalDate;

/**
 *
 * @author matte
 */
public class Agent {
    private int id_agent;
    private String pseudo;
    private String nom;
    private String specialite;
    private int niveau;
    private boolean actif;
    
    public Agent(int unid_agent, String unPseudo, String unNom, String uneSpecialite, int unNiveau, boolean unActif){
        this.id_agent = unid_agent;
        this.pseudo = unPseudo;
        this.nom = unNom;
        this.specialite = uneSpecialite;
        this.niveau = unNiveau;
        this.actif = unActif;
    }
    
    public Agent(String unPseudo, String unNom, String uneSpecialite, int unNiveau, boolean unActif){
        this.pseudo = unPseudo;
        this.nom = unNom;
        this.specialite = uneSpecialite;
        this.niveau = unNiveau;
        this.actif = unActif;
    }
    
    public Agent(){
    }

    public int getId_agent() {
        return id_agent;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getNom() {
        return nom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public int getNiveau() {
        return niveau;
    }

    public boolean isActif() {
        return actif;
    }

    public void setId_agent(int id_agent) {
        this.id_agent = id_agent;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "Agent{" + "id_agent=" + id_agent + ", pseudo=" + pseudo + ", nom=" + nom + ", specialite=" + specialite + ", niveau=" + niveau + ", actif=" + actif + '}';
    }
}
