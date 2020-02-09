import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.io.*;

public class Menu extends JFrame implements ActionListener {

	private static JButton partieRapid;
	private static JButton partiePerso;

  	public Menu() {
		
    	this.setTitle("Jeu de GO");
    	this.setSize(300, 300);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setLocationRelativeTo(null);

    	JLabel sautLigne = new JLabel("<html><br><br><br>");
    	sautLigne.setFont(new Font("Serif", Font.PLAIN, 20));

		JLabel sautLigne2 = new JLabel("<html><br><br><br>");
    	sautLigne.setFont(new Font("Serif", Font.PLAIN, 20));

    	JPanel b1 = new JPanel();
    	//On définit le layout en lui indiquant qu'il travaillera en ligne
	    b1.setLayout(new BoxLayout(b1, BoxLayout.LINE_AXIS));
	    partieRapid = new JButton("Partie rapide");
	    b1.add(partieRapid);

	    JPanel b2 = new JPanel();
	    //Idem pour cette ligne
	    b2.setLayout(new BoxLayout(b2, BoxLayout.LINE_AXIS));
	    partiePerso = new JButton("Partie personnalisée");
	    b2.add(partiePerso);

	    JPanel b4 = new JPanel();
	    //On positionne maintenant ces trois lignes en colonne
	    b4.setLayout(new BoxLayout(b4, BoxLayout.PAGE_AXIS));
	    b4.add(sautLigne);
	    b4.add(b1);
	    b4.add(sautLigne2);
	    b4.add(b2);

	    Color backgroundColor = new Color(160, 82, 45);
	    b4.setBackground(backgroundColor);

	    this.getContentPane().add(b4);
	    this.setVisible(true);  

	    // Action des boutons 
        partieRapid.addActionListener(this);
        partiePerso.addActionListener(this);
	}

  	// Actions des boutons
    public void actionPerformed(ActionEvent e) {
        // Choisir la source du bouton (Passer ou Abandonner)
        Object source = e.getSource();

        if(source == partieRapid) {
        	// System.out.println("Lancement d'une partie rapide");
            
            // Création de la fenêtre
            JFrame fenetre = new JFrame();
            fenetre.setSize(new Dimension(1920, 1080));
            fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fenetre.setLayout(null);

            // Passage des paramètres
            Jeu jeu = new Jeu(19, 0, 1);
            fenetre.setContentPane(jeu);
            
            fenetre.setVisible(true);

            // Fermer cette fenêtre
        	this.dispose();
        } else if (source == partiePerso) {
        	// System.out.println("Chargement des options");

            // Création de la fenêtre
            PartiePerso partiePerso = new PartiePerso();

            partiePerso.setTitle("Options de la partie");
            partiePerso.setSize(800, 320);
            partiePerso.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            partiePerso.setLocationRelativeTo(null);
            partiePerso.setVisible(true);

            // Fermer cette fenêtre
            this.dispose();
        }
    }
  
  	public static void main(String[] args) {
    	Menu menu = new Menu();
  	}
}