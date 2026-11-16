package gestionmission.IHM;

import gestionmission.DAO.AgentDAO;
import gestionmission.Metier.Agent;
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

public class ConsulterAgent extends JFrame {

    private final JTable tableau = new JTable();

    public ConsulterAgent() {
        super("Consulter les agents");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableau.setAutoCreateRowSorter(true);
        tableau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    ouvrirAgentSelectionne(tableau.rowAtPoint(event.getPoint()));
                }
            }
        });
        add(new JScrollPane(tableau), BorderLayout.CENTER);

        JButton retour = new JButton("Retour");
        retour.addActionListener(event -> {
            new MenuAgents().setVisible(true);
            dispose();
        });
        add(retour, BorderLayout.SOUTH);
        ((javax.swing.JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        try {
            chargerAgents();
            setSize(700, 400);
            setLocationRelativeTo(null);
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des agents : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void chargerAgents() throws SQLException {
        DefaultTableModel modele = new DefaultTableModel(
                new Object[]{"ID", "Pseudo", "Nom", "Spécialité", "Niveau", "Actif"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Agent agent : new AgentDAO().listerPreparer()) {
            modele.addRow(new Object[]{agent.getId_agent(), agent.getPseudo(), agent.getNom(),
                agent.getSpecialite(), agent.getNiveau(), agent.isActif() ? "Oui" : "Non"});
        }
        tableau.setModel(modele);
    }

    private void ouvrirAgentSelectionne(int ligneVue) {
        if (ligneVue < 0) {
            return;
        }
        int ligneModele = tableau.convertRowIndexToModel(ligneVue);
        int idAgent = (Integer) tableau.getModel().getValueAt(ligneModele, 0);
        try {
            Agent agent = new AgentDAO().trouverParId(idAgent);
            if (agent == null) {
                JOptionPane.showMessageDialog(this, "Cet agent n'existe plus.", "Information", JOptionPane.INFORMATION_MESSAGE);
                chargerAgents();
                return;
            }
            ModifierAgentDialogue dialogue = new ModifierAgentDialogue(this, true, agent);
            dialogue.setVisible(true);
            chargerAgents();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture de l'agent : " + exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
