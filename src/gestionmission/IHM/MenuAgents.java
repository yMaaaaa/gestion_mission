package gestionmission.IHM;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MenuAgents extends JFrame {

    public MenuAgents() {
        super("Gestion des agents");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton ajouter = new JButton("Ajouter un agent");
        JButton consulter = new JButton("Consulter les agents");
        JButton retour = new JButton("Retour");

        ajouter.addActionListener(event -> {
            new AjouterAgent().setVisible(true);
            dispose();
        });
        consulter.addActionListener(event -> {
            new ConsulterAgent().setVisible(true);
            dispose();
        });
        retour.addActionListener(event -> dispose());

        JPanel contenu = new JPanel(new GridLayout(3, 1, 10, 10));
        contenu.add(ajouter);
        contenu.add(consulter);
        contenu.add(retour);
        contenu.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(contenu);
        pack();
        setLocationRelativeTo(null);
    }
}
