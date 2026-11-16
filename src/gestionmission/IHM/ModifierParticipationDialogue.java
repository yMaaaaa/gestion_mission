package gestionmission.IHM;

import gestionmission.DAO.ParticipationDAO;
import gestionmission.Metier.Participation;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ModifierParticipationDialogue extends JDialog {

    private final Participation participation;
    private final JTextField role = new JTextField(20);
    private final JTextField dateAffectation = new JTextField(20);

    public ModifierParticipationDialogue(java.awt.Frame parent, boolean modal, Participation participation) {
        super(parent, "Modifier une participation", modal);
        this.participation = participation;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        role.setText(participation.getRole());
        dateAffectation.setText(participation.getDate_affectation().toString());

        JPanel formulaire = new JPanel(new GridBagLayout());
        ajouterLigne(formulaire, 0, "ID agent :", new JLabel(String.valueOf(participation.getId_agent())));
        ajouterLigne(formulaire, 1, "ID mission :", new JLabel(String.valueOf(participation.getId_mission())));
        ajouterLigne(formulaire, 2, "Rôle :", role);
        ajouterLigne(formulaire, 3, "Date d'affectation (AAAA-MM-JJ) :", dateAffectation);

        JButton modifier = new JButton("Modifier");
        JButton supprimer = new JButton("Supprimer");
        JButton annuler = new JButton("Annuler");
        modifier.addActionListener(event -> modifierParticipation());
        supprimer.addActionListener(event -> supprimerParticipation());
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

    private void modifierParticipation() {
        String roleSaisi = role.getText().trim();
        if (roleSaisi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le rôle est obligatoire.", "Saisie invalide", JOptionPane.WARNING_MESSAGE);
            return;
        }

        final LocalDate date;
        try {
            date = LocalDate.parse(dateAffectation.getText().trim());
        } catch (java.time.format.DateTimeParseException exception) {
            JOptionPane.showMessageDialog(this, "La date doit respecter le format AAAA-MM-JJ.", "Saisie invalide", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Participation modification = new Participation(
                participation.getId_agent(), participation.getId_mission(), roleSaisi, date);
        try {
            if (new ParticipationDAO().modifier(modification)) {
                JOptionPane.showMessageDialog(this, "Participation modifiée.", "Information", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "La participation n'a pas été modifiée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification de la participation : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerParticipation() {
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Supprimer cette participation ? Cette action est définitive.",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (new ParticipationDAO().supprimer(participation.getId_agent(), participation.getId_mission())) {
                JOptionPane.showMessageDialog(this, "Participation supprimée.", "Information", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "La participation n'a pas été supprimée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la participation : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void ajouterLigne(JPanel panneau, int ligne, String libelle, java.awt.Component champ) {
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
