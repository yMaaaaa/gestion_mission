package gestionmission.IHM;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MenuParticipation extends JFrame {

    public MenuParticipation() {
        super("Gestion des participations");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton affecter = new JButton("Affecter un agent à une mission");
        JButton consulter = new JButton("Consulter les participations");
        JButton retour = new JButton("Retour");

        affecter.addActionListener(event -> {
            new AffecterAgentMission().setVisible(true);
            dispose();
        });
        consulter.addActionListener(event -> {
            new ConsulterParticipation().setVisible(true);
            dispose();
        });
        retour.addActionListener(event -> dispose());

        JPanel contenu = new JPanel(new GridLayout(3, 1, 10, 10));
        contenu.add(affecter);
        contenu.add(consulter);
        contenu.add(retour);
        contenu.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(contenu);
        pack();
        setLocationRelativeTo(null);
    }
}
