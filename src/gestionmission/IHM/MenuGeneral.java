package gestionmission.IHM;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MenuGeneral extends JFrame {

    public MenuGeneral() {
        super("Gestion des missions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton missions = new JButton("Gestion des missions");
        JButton participations = new JButton("Gestion des participations");
        JButton agents = new JButton("Gestion des agents");
        JButton quitter = new JButton("Quitter");

        missions.addActionListener(event -> new MenuMission().setVisible(true));
        participations.addActionListener(event -> new MenuParticipation().setVisible(true));
        agents.addActionListener(event -> new MenuAgents().setVisible(true));
        quitter.addActionListener(event -> dispose());

        JPanel contenu = new JPanel(new GridLayout(4, 1, 10, 10));
        contenu.add(missions);
        contenu.add(participations);
        contenu.add(agents);
        contenu.add(quitter);
        contenu.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(contenu);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new MenuGeneral().setVisible(true));
    }
}
