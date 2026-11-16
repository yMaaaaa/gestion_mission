package gestionmission.IHM;

import gestionmission.DAO.AgentDAO;
import gestionmission.Metier.Agent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AjouterAgent extends JFrame {

    private final JTextField pseudo = new JTextField(20);
    private final JTextField nom = new JTextField(20);
    private final JTextField specialite = new JTextField(20);
    private final JTextField niveau = new JTextField(20);
    private final JCheckBox actif = new JCheckBox("Agent actif", true);

    public AjouterAgent() {
        super("Ajouter un agent");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formulaire = new JPanel(new GridBagLayout());
        ajouterLigne(formulaire, 0, "Pseudo :", pseudo);
        ajouterLigne(formulaire, 1, "Nom :", nom);
        ajouterLigne(formulaire, 2, "Spécialité :", specialite);
        ajouterLigne(formulaire, 3, "Niveau :", niveau);
        GridBagConstraints cases = new GridBagConstraints();
        cases.gridx = 1;
        cases.gridy = 4;
        cases.anchor = GridBagConstraints.WEST;
        cases.insets = new Insets(5, 5, 5, 5);
        formulaire.add(actif, cases);

        JButton ajouter = new JButton("Ajouter");
        JButton retour = new JButton("Annuler");
        ajouter.addActionListener(event -> ajouterAgent());
        retour.addActionListener(event -> retourMenu());

        JPanel actions = new JPanel();
        actions.add(ajouter);
        actions.add(retour);
        setLayout(new java.awt.BorderLayout(10, 10));
        add(formulaire, java.awt.BorderLayout.CENTER);
        add(actions, java.awt.BorderLayout.SOUTH);
        ((JPanel) getContentPane()).setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pack();
        setLocationRelativeTo(null);
    }

    private void ajouterAgent() {
        String pseudoSaisi = pseudo.getText().trim();
        String nomSaisi = nom.getText().trim();
        String specialiteSaisie = specialite.getText().trim();
        if (pseudoSaisi.isEmpty() || nomSaisi.isEmpty() || specialiteSaisie.isEmpty() || niveau.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Saisie invalide", JOptionPane.WARNING_MESSAGE);
            return;
        }

        final int niveauSaisi;
        try {
            niveauSaisi = Integer.parseInt(niveau.getText().trim());
            if (niveauSaisi < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Le niveau doit être un entier positif ou nul.", "Saisie invalide", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Agent agent = new Agent(pseudoSaisi, nomSaisi, specialiteSaisie, niveauSaisi, actif.isSelected());
        try {
            int resultat = new AgentDAO().ajouter(agent);
            if (resultat == 1) {
                JOptionPane.showMessageDialog(this, "Agent ajouté.", "Information", JOptionPane.INFORMATION_MESSAGE);
                retourMenu();
            } else {
                JOptionPane.showMessageDialog(this, "L'agent n'a pas été ajouté.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (java.sql.SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'agent : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retourMenu() {
        new MenuAgents().setVisible(true);
        dispose();
    }

    private static void ajouterLigne(JPanel panneau, int ligne, String libelle, JTextField champ) {
        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.insets = new Insets(5, 5, 5, 5);
        contraintes.gridy = ligne;
        contraintes.gridx = 0;
        contraintes.anchor = GridBagConstraints.LINE_END;
        panneau.add(new JLabel(libelle), contraintes);
        contraintes.gridx = 1;
        contraintes.anchor = GridBagConstraints.LINE_START;
        panneau.add(champ, contraintes);
    }
}
