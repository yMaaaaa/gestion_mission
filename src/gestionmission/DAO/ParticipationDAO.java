/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestionmission.DAO;

import gestionmission.Connexion.ConnexionBDD;
import gestionmission.Metier.Participation;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author matte
 */
public class ParticipationDAO {

    public ParticipationDAO() {
    }

    public ArrayList<Participation> listerPreparer() throws SQLException {       
            String sql = "SELECT * FROM participation ORDER BY date_affectation, id_mission, id_agent";
            ArrayList<Participation> lesParticipations = new ArrayList<Participation>();
            Connection connexion = ConnexionBDD.obtenirConnexion();
            PreparedStatement requete = connexion.prepareStatement(sql);
            ResultSet lesResultats = requete.executeQuery();           
            Participation uneParticipation;
            while(lesResultats.next()){
                uneParticipation=new Participation(lesResultats.getInt("id_agent"),
                lesResultats.getInt("id_mission"),
                lesResultats.getString("role"),
                lesResultats.getDate("date_affectation").toLocalDate());
                lesParticipations.add(uneParticipation);    
            }           
        return lesParticipations;
    }
    
    public Participation trouverParId(int idAgent, int idMission) throws SQLException {
        String sql = "SELECT * FROM participation WHERE id_agent = ? AND id_mission = ?";

        try (Connection connexion = ConnexionBDD.obtenirConnexion(); PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, idAgent);
            requete.setInt(2, idMission);
            try (
                    ResultSet resultat
                    = requete.executeQuery()) {
                if (resultat.next()) {
                    return construireParticipation(resultat);
                }

                return null;
            }
        }
    }
     
    public int ajouter(Participation participation) throws SQLException {
        String sql = "INSERT INTO participation "
                + "(id_agent, id_mission, role, date_affectation) "
                + "VALUES (?, ?, ?, ?)";
        int rep;
        Connection connexion =ConnexionBDD.obtenirConnexion();     
        PreparedStatement requete = connexion.prepareStatement(sql);
        requete.setInt(1, participation.getId_agent());
        requete.setInt(2, participation.getId_mission());
        requete.setString(3, participation.getRole());
        requete.setDate(4, Date.valueOf(participation.getDate_affectation()));
        rep=requete.executeUpdate();
        return rep;
    }
    
    public ArrayList<Participation> listerParMission(int idMission) throws SQLException {
        String sql = "SELECT * FROM participation WHERE id_mission = ? ORDER BY date_affectation, id_agent";
        ArrayList<Participation> lesParticipations = new ArrayList<Participation>();
        try (Connection connexion = ConnexionBDD.obtenirConnexion(); PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, idMission);
            try (
                    ResultSet lesResultats
                    = requete.executeQuery()) {
                while (lesResultats.next()) {
                    lesParticipations.add(construireParticipation(lesResultats));
                }
            }
        }
        return lesParticipations;
    }
    
    public boolean modifier(Participation uneParticipation) throws SQLException {
        String sql = "UPDATE participation SET role=?, date_affectation=? WHERE id_agent=? AND id_mission=?";
        int rep;
        boolean reponse;
        try(
        Connection connexion = ConnexionBDD.obtenirConnexion();
        PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, uneParticipation.getRole());
            requete.setDate(2, Date.valueOf(uneParticipation.getDate_affectation()));
            requete.setInt(3, uneParticipation.getId_agent());
            requete.setInt(4, uneParticipation.getId_mission());
            rep = requete.executeUpdate();
        }
        if (rep==1){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean supprimer(int idAgent, int idMission) throws SQLException{
        String sql = "DELETE FROM participation WHERE id_agent = ? AND id_mission = ?";
        int rep;
        try (
                Connection connexion = ConnexionBDD.obtenirConnexion();
                PreparedStatement requete = connexion.prepareStatement(sql)) {
                    requete.setInt(1, idAgent);
                    requete.setInt(2, idMission);
                    rep = requete.executeUpdate();
        }
        if(rep==1){
            return true;
        } else {
            return false;
        }
    }
    
    private void renseignerRequete(PreparedStatement requete, Participation participation) throws SQLException {
        requete.setInt(1, participation.getId_agent());
        requete.setInt(2, participation.getId_mission());
        requete.setString(3, participation.getRole());
        requete.setDate(4, Date.valueOf(participation.getDate_affectation()));
    }
    
    private Participation construireParticipation(ResultSet resultat) throws SQLException {
        Participation participation = new Participation();
        participation.setId_agent(resultat.getInt("id_agent"));
        participation.setId_mission(resultat.getInt("id_mission"));
        participation.setRole(resultat.getString("role"));
        participation.setDate_affectation(resultat.getDate("date_affectation").toLocalDate());
        return participation;
    }
}