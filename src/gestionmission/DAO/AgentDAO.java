/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestionmission.DAO;

import gestionmission.Connexion.ConnexionBDD;
import gestionmission.Metier.Agent;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author matte
 */
public class AgentDAO {

    public AgentDAO() {
    }

    public ArrayList<Agent> listerPreparer() throws SQLException {       
            String sql = "SELECT * FROM agent ORDER BY id_agent";
            ArrayList<Agent> lesAgents = new ArrayList<Agent>();
            Connection connexion = ConnexionBDD.obtenirConnexion();
            PreparedStatement requete = connexion.prepareStatement(sql);
            ResultSet lesResultats = requete.executeQuery();           
            Agent unAgent;
            while(lesResultats.next()){
                unAgent=new Agent(lesResultats.getInt("id_agent"),
                lesResultats.getString("pseudo"),
                lesResultats.getString("nom"),
                lesResultats.getString("specialite"),
                lesResultats.getInt("niveau"),
                lesResultats.getBoolean("actif"));
                lesAgents.add(unAgent);    
            }           
        return lesAgents;
    }
    
    public Agent trouverParId(int id) throws SQLException {
        String sql = "SELECT * FROM agent WHERE id_agent = ?";

        try (Connection connexion = ConnexionBDD.obtenirConnexion(); PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, id);
            try (
                    ResultSet resultat
                    = requete.executeQuery()) {
                if (resultat.next()) {
                    return construireAgent(resultat);
                }

                return null;
            }
        }
    }
     
    public int ajouter(Agent agent) throws SQLException {
        String sql = "INSERT INTO agent "
                + "(pseudo, nom, specialite, niveau, actif) "
                + "VALUES (?, ?, ?, ?, ?)";
        int rep;
        Connection connexion =ConnexionBDD.obtenirConnexion();     
        PreparedStatement requete = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        requete.setString(1, agent.getPseudo());
        requete.setString(2, agent.getNom());
        requete.setString(3, agent.getSpecialite());
        requete.setInt(4, agent.getNiveau());
        requete.setBoolean(5, agent.isActif());
        rep=requete.executeUpdate();
        
        ResultSet cles = requete.getGeneratedKeys();
        if (cles.next()) {
            agent.setId_agent(cles.getInt(1));
            }
        return rep;
    }
     
    public ArrayList<Agent> listerParMission(int idMission) throws SQLException {
        String sql = "SELECT * FROM agent INNER JOIN participation ON agent.id_agent = participation.id_agent WHERE participation.id_mission = ? ORDER BY agent.nom, agent.pseudo";
        ArrayList<Agent> lesAgents = new ArrayList<Agent>();
        try (Connection connexion = ConnexionBDD.obtenirConnexion(); PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, idMission);
            try (ResultSet lesResultats = requete.executeQuery()) {
                while (lesResultats.next()) {
                    lesAgents.add(construireAgent(lesResultats));
                }
            }
        }
        return lesAgents;
    }
    
    public boolean modifier(Agent unAgent) throws SQLException {
        String sql = "UPDATE agent SET pseudo=?, nom=?, specialite=?, niveau=?, actif=? WHERE id_agent=?";
        int rep;
        boolean reponse;
        try(
        Connection connexion = ConnexionBDD.obtenirConnexion();
        PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, unAgent.getPseudo());
            requete.setString(2, unAgent.getNom());
            requete.setString(3, unAgent.getSpecialite());
            requete.setInt(4, unAgent.getNiveau());
            requete.setBoolean(5, unAgent.isActif());
            requete.setInt(6, unAgent.getId_agent());
            rep = requete.executeUpdate();
        }
        if (rep==1){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean supprimer(int id) throws SQLException{
        String sql = "DELETE FROM agent WHERE id_agent = ?";
        int rep;
        try (
                Connection connexion = ConnexionBDD.obtenirConnexion();
                PreparedStatement requete = connexion.prepareStatement(sql)) {
                    requete.setInt(1, id);
                    rep = requete.executeUpdate();
        }
        if(rep==1){
            return true;
        } else {
            return false;
        }
    }
    
    private void renseignerRequete(PreparedStatement requete, Agent agent) throws SQLException {
        requete.setString(1, agent.getPseudo());
        requete.setString(2, agent.getNom());
        requete.setString(3, agent.getSpecialite());
        requete.setInt(4, agent.getNiveau());
        requete.setBoolean(5, agent.isActif());
    }
    
    private Agent construireAgent(ResultSet resultat) throws SQLException {
        Agent agent = new Agent();
        agent.setId_agent(resultat.getInt("id_agent"));
        agent.setPseudo(resultat.getString("pseudo"));
        agent.setNom(resultat.getString("nom"));
        agent.setSpecialite(resultat.getString("specialite"));
        agent.setNiveau(resultat.getInt("niveau"));
        agent.setActif(resultat.getBoolean("actif"));
        return agent;
    }
}