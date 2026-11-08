/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestionmission.Connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnexionBDD {
    private static final String URL =
            "jdbc:mysql://localhost:3306/gestion_missions?serverTimezone=Europe/Paris&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    private ConnexionBDD() {
    }

    public static Connection obtenirConnexion() throws SQLException {
        return DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
    }
}
