/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestionmission.DAO;

import gestionmission.Connexion.ConnexionBDD;
import gestionmission.Metier.Mission;
import gestionmission.Metier.Priorite;
import gestionmission.Metier.Statut;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author matteo.douteaud
 */
public class MissionDAO {

    public MissionDAO() {
    }
    
//    public ArrayList<Mission> lister() throws SQLException{
//        
//        String sql = "SELECT * FROM mission ORDER BY date_debut, titre";
//                Connection connexion = ConnexionBDD.obtenirConnexion();
//                Statement transmission = connexion.createStatement();
//                ResultSet lesResultats = transmission.executeQuery(sql);
//            
//                ArrayList<Mission> lesMissions = new ArrayList<Mission>();
//                //il faut se positionner sur la première ligne d'enregistrement au début
//                //sinon ce sont les champs qu'on tente de récupérer, id, titre, description etc...
//                Mission uneMission;
//                while (lesResultats.next()) {
//                    uneMission = new Mission(lesResultats.getInt("id"),
//                    lesResultats.getString("titre"), 
//                    lesResultats.getString("description"), 
//                    lesResultats.getDate("date_debut").toLocalDate(), 
//                    lesResultats.getDate("date_fin").toLocalDate(), 
//                    Statut.valueOf(lesResultats.getString("statut")), 
//                    Priorite.valueOf(lesResultats.getString("priorite")), 
//                    lesResultats.getDouble("budget"));
//                    lesMissions.add(uneMission);
//                }
//            return lesMissions;
//        }
    public ArrayList<Mission> listerPreparer() throws SQLException {       
            String sql = "SELECT * FROM mission ORDER BY date_debut, titre";
            ArrayList<Mission> lesMissions = new ArrayList<Mission>();
            Connection connexion = ConnexionBDD.obtenirConnexion();
            PreparedStatement requete = connexion.prepareStatement(sql);
            ResultSet lesResultats = requete.executeQuery();           
            Mission uneMission;
            while(lesResultats.next()){
                uneMission=new Mission(lesResultats.getInt("id"),
                lesResultats.getString("titre"),
                lesResultats.getString("description"),lesResultats.getDate("date_debut").toLocalDate(),
                lesResultats.getDate("date_fin").toLocalDate(),
                Statut.valueOf(lesResultats.getString("statut")),
                Priorite.valueOf(lesResultats.getString("priorite")),
                lesResultats.getDouble("budget"));
                lesMissions.add(uneMission);    
            }           
        return lesMissions;
    }
    
    public Mission trouverParId(int id) throws SQLException {
        String sql = "SELECT * FROM mission WHERE id = ?";

        try (Connection connexion = ConnexionBDD.obtenirConnexion(); PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, id);
            try (
                    ResultSet resultat
                    = requete.executeQuery()) {
                if (resultat.next()) {
                    return construireMission(resultat);
                }

                return null;
            }
        }
    }
     
    public int ajouter(Mission mission) throws SQLException {
        String sql = "INSERT INTO mission "
                + "(titre, description, date_debut, date_fin, statut, priorite, budget) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        int rep;
        Connection connexion =ConnexionBDD.obtenirConnexion();     
        PreparedStatement requete = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        requete.setString(1, mission.getTitre());
        requete.setString(2, mission.getDescription());
        requete.setDate(3, Date.valueOf(mission.getDateDebut()));
        requete.setDate(4, Date.valueOf(mission.getDateFin()));
        requete.setString(5, mission.getStatut().name());
        requete.setString(6, mission.getPriorite().name());
        requete.setDouble(7, mission.getBudget());
        rep=requete.executeUpdate();
        
        ResultSet cles = requete.getGeneratedKeys();
        if (cles.next()) {
            mission.setId(cles.getInt(1));
            }
        return rep;
    }

    
   /* public int ajouter2(Mission uneMission) throws SQLException {
        System.out.println(uneMission.toString());
        String sql = "INSERT INTO mission (titre, description, date_debut, date_fin, statut, priorite, budget)" 
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";
       String sql = "INSERT INTO `mission` (`titre`, `description`, `date_debut`, `date_fin`, `statut`, `priorite`, `budget`) VALUES(?,?,?,?,?,?,?)";
        String sql = "Insert into Mission (titre,description,date_debut,date_fin, statut, priorite,budget) VALUES (?,?,?,?,?,?,?)";
       System.out.println(sql);
        int rep;
        Connection connexion = ConnexionBDD.obtenirConnexion();
        PreparedStatement requete = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        requete.setString(1, uneMission.getTitre());
        requete.setString(2, uneMission.getDescription());
        requete.setDate(3, Date.valueOf(uneMission.getDateDebut()));
        requete.setDate(4, Date.valueOf(uneMission.getDateFin()));
        requete.setString(5, uneMission.getStatut().name());
        requete.setString(6, uneMission.getPriorite().name());
        requete.setDouble(7, uneMission.getBudget());
        rep = requete.executeUpdate(sql);
        
        ResultSet cles = requete.getGeneratedKeys();
        if(cles.next()){
            uneMission.setId(cles.getInt(1));
        }
        return rep;
    }*/
    
    public boolean modifier(Mission uneMission) throws SQLException {
        String sql = "UPDATE mission SET titre=?, description=?, date_debut=?, date_fin=?, statut=?, priorite=?, budget=? WHERE id=?";
        int rep;
        boolean reponse;
        try(
        Connection connexion = ConnexionBDD.obtenirConnexion();
        PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, uneMission.getTitre());
            requete.setString(2, uneMission.getDescription());
            requete.setDate(3, Date.valueOf(uneMission.getDateDebut()));
            requete.setDate(4, Date.valueOf(uneMission.getDateFin()));
            requete.setString(5, uneMission.getStatut().name());
            requete.setString(6, uneMission.getPriorite().name());
            requete.setDouble(7, uneMission.getBudget());
            requete.setInt(8, uneMission.getId());
            rep = requete.executeUpdate();
        }
        if (rep==1){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean supprimer(int id) throws SQLException{
        String sql = "DELETE FROM mission WHERE id = ?";
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
    
    private void renseignerRequete(PreparedStatement requete, Mission mission) throws SQLException {
        requete.setString(1, mission.getTitre());
        requete.setString(2, mission.getDescription());
        requete.setDate(3, Date.valueOf(mission.getDateDebut()));
        requete.setDate(4, Date.valueOf(mission.getDateFin()));
        requete.setString(5, mission.getStatut().name());
        requete.setString(6, mission.getPriorite().name());
        requete.setDouble(7, mission.getBudget());
    }
    
    private Mission construireMission(ResultSet resultat) throws SQLException {
        Mission mission = new Mission();
        mission.setId(resultat.getInt("id"));
        mission.setTitre(resultat.getString("titre"));
        mission.setDescription(resultat.getString("description"));
        mission.setDateDebut(resultat.getDate("date_debut").toLocalDate());
        mission.setDateFin(resultat.getDate("date_fin").toLocalDate());
        mission.setStatut(Statut.valueOf(resultat.getString("statut")));
        mission.setPriorite(Priorite.valueOf(resultat.getString("priorite")));
        mission.setBudget(resultat.getDouble("budget"));
        return mission;
    }
}