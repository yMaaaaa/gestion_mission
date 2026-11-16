package gestionmission.IHM;

import gestionmission.DAO.ParticipationDAO;
import gestionmission.Metier.Participation;
import java.awt.BorderLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConsulterParticipation extends JFrame {

    private final JTable tableau = new JTable();

    public ConsulterParticipation() {
        super("Consulter les participations");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableau.setAutoCreateRowSorter(true);
        tableau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    ouvrirParticipationSelectionnee(tableau.rowAtPoint(event.getPoint()));
                }
            }
        });
        add(new JScrollPane(tableau), BorderLayout.CENTER);

        JButton retour = new JButton("Retour");
        retour.addActionListener(event -> {
            new MenuParticipation().setVisible(true);
            dispose();
        });
        add(retour, BorderLayout.SOUTH);
        ((javax.swing.JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        try {
            chargerParticipations();
            setSize(600, 400);
            setLocationRelativeTo(null);
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des participations : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void chargerParticipations() throws SQLException {
        DefaultTableModel modele = new DefaultTableModel(
                new Object[]{"ID agent", "ID mission", "Rôle", "Date d'affectation"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Participation participation : new ParticipationDAO().listerPreparer()) {
            modele.addRow(new Object[]{participation.getId_agent(), participation.getId_mission(),
                participation.getRole(), participation.getDate_affectation()});
        }
        tableau.setModel(modele);
    }

    private void ouvrirParticipationSelectionnee(int ligneVue) {
        if (ligneVue < 0) {
            return;
        }
        int ligneModele = tableau.convertRowIndexToModel(ligneVue);
        int idAgent = (Integer) tableau.getModel().getValueAt(ligneModele, 0);
        int idMission = (Integer) tableau.getModel().getValueAt(ligneModele, 1);
        try {
            Participation participation = new ParticipationDAO().trouverParId(idAgent, idMission);
            if (participation == null) {
                JOptionPane.showMessageDialog(this, "Cette participation n'existe plus.", "Information", JOptionPane.INFORMATION_MESSAGE);
                chargerParticipations();
                return;
            }
            ModifierParticipationDialogue dialogue = new ModifierParticipationDialogue(this, true, participation);
            dialogue.setVisible(true);
            chargerParticipations();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture de la participation : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
