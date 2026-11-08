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
public class Participation {
    private int id_agent;
    private int id_mission;
    private String role;
    private LocalDate date_affectation;
    
    public Participation (int unid_agent, int unid_mission, String unRole, LocalDate uneDate_affectation) {
        this.id_agent = unid_agent;
        this.id_mission = unid_mission;
        this.role = unRole;
        this.date_affectation = uneDate_affectation;
    }
    
    public Participation (String unRole, LocalDate uneDate_affectation) {
        this.role = unRole;
        this.date_affectation = uneDate_affectation;
    }
    
    public Participation () {
    }

    public int getId_agent() {
        return id_agent;
    }

    public int getId_mission() {
        return id_mission;
    }

    public String getRole() {
        return role;
    }

    public LocalDate getDate_affectation() {
        return date_affectation;
    }

    public void setId_agent(int id_agent) {
        this.id_agent = id_agent;
    }

    public void setId_mission(int id_mission) {
        this.id_mission = id_mission;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDate_affectation(LocalDate date_affectation) {
        this.date_affectation = date_affectation;
    }

    @Override
    public String toString() {
        return "Participation{" + "id_agent=" + id_agent + ", id_mission=" + id_mission + ", role=" + role + ", date_affectation=" + date_affectation + '}';
    }
}
