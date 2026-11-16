package gestionmission.IHM;

import gestionmission.DAO.AgentDAO;
import gestionmission.Metier.Agent;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ModifierAgentDialogue extends JDialog {

    private final Agent agent;
    private final JTextField pseudo = new JTextField(20);
    private final JTextField nom = new JTextField(20);
    private final JTextField specialite = new JTextField(20);
    private final JTextField niveau = new JTextField(20);
    private final JCheckBox actif = new JCheckBox("Agent actif");

    public ModifierAgentDialogue(java.awt.Frame parent, boolean modal, Agent agent) {
        super(parent, "Modifier un agent", modal);
        this.agent = agent;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        pseudo.setText(agent.getPseudo());
        nom.setText(agent.getNom());
        specialite.setText(agent.getSpecialite());
        niveau.setText(String.valueOf(agent.getNiveau()));
        actif.setSelected(agent.isActif());

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

        JButton modifier = new JButton("Modifier");
        JButton supprimer = new JButton("Supprimer");
        JButton annuler = new JButton("Annuler");
        modifier.addActionListener(event -> modifierAgent());
        supprimer.addActionListener(event -> supprimerAgent());
        annuler.addActionListener(event -> dispose());

        JPanel actions = new JPanel();
        actions.add(modifier);
        actions.add(supprimer);
        actions.add(annuler);

        setLayout(new BorderLayout(10, 10));
        add(formulaire, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pack();
        setLocationRelativeTo(parent);
    }

    private void modifierAgent() {
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

        Agent modification = new Agent(agent.getId_agent(), pseudoSaisi, nomSaisi, specialiteSaisie, niveauSaisi, actif.isSelected());
        try {
            if (new AgentDAO().modifier(modification)) {
                JOptionPane.showMessageDialog(this, "Agent modifié.", "Information", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "L'agent n'a pas été modifié.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'agent : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerAgent() {
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Supprimer cet agent ? Cette action est définitive.",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (new AgentDAO().supprimer(agent.getId_agent())) {
                JOptionPane.showMessageDialog(this, "Agent supprimé.", "Information", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "L'agent n'a pas été supprimé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'agent : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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
