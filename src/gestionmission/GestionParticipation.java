/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestionmission;
import java.sql.SQLException;
import java.sql.Connection;
import gestionmission.Connexion.ConnexionBDD;
import gestionmission.DAO.ParticipationDAO;
import gestionmission.Metier.Participation;
import java.time.LocalDate;
import java.util.ArrayList;
/**
 *
 * @author matte
 */
public class GestionParticipation {
    public static void main(String[] args) {
        try (Connection connexion = ConnexionBDD.obtenirConnexion()) {
            System.out.println("Connexion reussie");
            
            ArrayList<Participation> lesParticipations = new ArrayList<Participation>();
            ParticipationDAO uneParticipationDAO = new ParticipationDAO();
            lesParticipations=uneParticipationDAO.listerPreparer();
            
            for(int i=0; i<lesParticipations.size();i++){
                System.out.println(lesParticipations.get(i).toString());
            }
            
            for(Participation uneParticipation:lesParticipations) {
                System.out.println(uneParticipation.toString());
            }
            
            // a adapter : l'agent (id_agent) et la mission (id_mission) doivent deja exister en base
            Participation uneParticipation = new Participation
            (1,
            1,
            "Chef d'equipe",
            LocalDate.now()
            );
            
            int resultat = uneParticipationDAO.ajouter(uneParticipation);
            if(resultat==1){
                System.out.println("Participation ajoutee");
            } else {
                System.out.println("Echec");
            }  
        } catch (SQLException exception) {
            System.out.println("Erreur : " + exception.getMessage());
        }
    }
}
