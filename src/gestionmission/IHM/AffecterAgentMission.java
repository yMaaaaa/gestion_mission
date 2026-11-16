package gestionmission.IHM;

import gestionmission.DAO.AgentDAO;
import gestionmission.DAO.MissionDAO;
import gestionmission.DAO.ParticipationDAO;
import gestionmission.Metier.Agent;
import gestionmission.Metier.Mission;
import gestionmission.Metier.Participation;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AffecterAgentMission extends JFrame {

    private final JComboBox<Agent> agents = new JComboBox<>();
    private final JComboBox<Mission> missions = new JComboBox<>();
    private final JTextField role = new JTextField(20);
    private final JTextField dateAffectation = new JTextField(LocalDate.now().toString(), 20);

    public AffecterAgentMission() {
        super("Affecter un agent à une mission");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configurerAffichage();

        JPanel formulaire = new JPanel(new GridBagLayout());
        ajouterLigne(formulaire, 0, "Agent :", agents);
        ajouterLigne(formulaire, 1, "Mission :", missions);
        ajouterLigne(formulaire, 2, "Rôle :", role);
        ajouterLigne(formulaire, 3, "Date d'affectation (AAAA-MM-JJ) :", dateAffectation);

        JButton valider = new JButton("Affecter");
        JButton retour = new JButton("Annuler");
        valider.addActionListener(event -> affecter());
        retour.addActionListener(event -> retourMenu());

        JPanel actions = new JPanel();
        actions.add(valider);
        actions.add(retour);
        add(formulaire, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        ((JPanel) getContentPane()).setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            chargerDonnees();
            pack();
            setLocationRelativeTo(null);
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des agents et missions : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void configurerAffichage() {
        agents.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> liste, Object valeur, int index, boolean selectionne, boolean focus) {
                super.getListCellRendererComponent(liste, valeur, index, selectionne, focus);
                if (valeur instanceof Agent) {
                    Agent agent = (Agent) valeur;
                    setText(agent.getPseudo() + " - " + agent.getNom());
                }
                return this;
            }
        });
        missions.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> liste, Object valeur, int index, boolean selectionne, boolean focus) {
                super.getListCellRendererComponent(liste, valeur, index, selectionne, focus);
                if (valeur instanceof Mission) {
                    Mission mission = (Mission) valeur;
                    setText(mission.getTitre() + " (ID " + mission.getId() + ")");
                }
                return this;
            }
        });
    }

    private void chargerDonnees() throws SQLException {
        for (Agent agent : new AgentDAO().listerPreparer()) {
            if (agent.isActif()) {
                agents.addItem(agent);
            }
        }
        for (Mission mission : new MissionDAO().listerPreparer()) {
            missions.addItem(mission);
        }
    }

    private void affecter() {
        Agent agent = (Agent) agents.getSelectedItem();
        Mission mission = (Mission) missions.getSelectedItem();
        String roleSaisi = role.getText().trim();
        if (agent == null || mission == null) {
            JOptionPane.showMessageDialog(this, "Il faut au moins un agent actif et une mission pour créer une affectation.", "Affectation impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }
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

        Participation participation = new Participation(agent.getId_agent(), mission.getId(), roleSaisi, date);
        try {
            int resultat = new ParticipationDAO().ajouter(participation);
            if (resultat == 1) {
                JOptionPane.showMessageDialog(this, "Agent affecté à la mission.", "Information", JOptionPane.INFORMATION_MESSAGE);
                retourMenu();
            } else {
                JOptionPane.showMessageDialog(this, "L'affectation n'a pas été enregistrée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'affectation : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retourMenu() {
        new MenuParticipation().setVisible(true);
        dispose();
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
