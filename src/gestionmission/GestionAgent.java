/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestionmission;
import java.sql.SQLException;
import java.sql.Connection;
import gestionmission.Connexion.ConnexionBDD;
import gestionmission.DAO.AgentDAO;
import gestionmission.Metier.Agent;
import java.util.ArrayList;
/**
 *
 * @author matte
 */
public class GestionAgent {
    public static void main(String[] args) {
        try (Connection connexion = ConnexionBDD.obtenirConnexion()) {
            System.out.println("Connexion reussie");
            
            ArrayList<Agent> lesAgents = new ArrayList<Agent>();
            AgentDAO unAgentDAO = new AgentDAO();
            lesAgents=unAgentDAO.listerPreparer();
            
            for(int i=0; i<lesAgents.size();i++){
                System.out.println(lesAgents.get(i).toString());
            }
            
            for(Agent unAgent:lesAgents) {
                System.out.println(unAgent.toString());
            }
            
            Agent unAgent = new Agent
            ("agent_test",
            "Agent de test DAO",
            "Infiltration",
            3,
            true
            );
            
            int resultat = unAgentDAO.ajouter(unAgent);
            if(resultat==1){
                System.out.println("Agent ajoute");
            } else {
                System.out.println("Echec");
            }
            
        } catch (SQLException exception) {
            System.out.println("Erreur : " + exception.getMessage());
        }
    }
}
