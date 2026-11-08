/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestionmission.Metier;
import java.time.LocalDate;

/**
 *
 * @author matteo.douteaud
 */
public class Mission {
    private int id;
    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Statut statut;
    private Priorite priorite;
    private double budget;

    public Mission(int id, String titre, String description, LocalDate dateDebut, LocalDate dateFin, Statut statut, Priorite priorite, double budget) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.priorite = priorite;
        this.budget = budget;
    }
    
    public Mission(String titre, String description, LocalDate dateDebut, LocalDate dateFin, Statut statut, Priorite priorite, double budget) {
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.priorite = priorite;
        this.budget = budget;
    }
    
    public Mission(){
        
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public Statut getStatut() {
        return statut;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public double getBudget() {
        return budget;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Mission : id : " + id + " | " + titre + " | " + description + " | " + dateDebut + " | " + dateFin + " | " + statut + " | " + priorite + " | " + budget + '}';
    }
    
}