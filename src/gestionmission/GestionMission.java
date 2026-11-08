/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestionmission;
import java.sql.SQLException;
import java.sql.Connection;
import gestionmission.Connexion.ConnexionBDD;
import gestionmission.DAO.MissionDAO;
import gestionmission.Metier.Mission;
import gestionmission.Metier.Priorite;
import gestionmission.Metier.Statut;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author matteo.douteaud
 */
public class GestionMission {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (Connection connexion = ConnexionBDD.obtenirConnexion()) {
            System.out.println("Connexion reussie");
            
            ArrayList<Mission> lesMissions = new ArrayList<Mission>();
            MissionDAO uneMissionDAO = new MissionDAO();
            lesMissions=uneMissionDAO.listerPreparer();
            
            for(int i=0; i<lesMissions.size();i++){
                System.out.println(lesMissions.get(i).toString());
            }
            
            for(Mission uneMission:lesMissions) {
                System.out.println(uneMission.toString());
            }
            
            Mission uneMission = new Mission
            ("Mission de test DAO",
            "Mission temporaire creee depuis la console",
            LocalDate.now(),
            LocalDate.now().plusDays(3),
            Statut.A_PREPARER,
            Priorite.HAUTE,
            2500.00
            );
            
            int resultat = uneMissionDAO.ajouter(uneMission);
            if(resultat==1){
                System.out.println("Mission ajoutee");
            } else {
                System.out.println("Echec");
            }
            
//            uneMission.setTitre("Mission modifiee");
//            boolean res = uneMissionDAO.modifier(uneMission);
//            if(res){
//                System.out.println("Mission modifee avec succes");
//            } else {
//                System.out.println("Modification echouee");
//            }
            
        } catch (SQLException exception) {
            System.out.println("Erreur : " + exception.getMessage());
        }
    }
}