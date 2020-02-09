import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.io.*;

public class PartiePerso extends JFrame implements ActionListener {

    // Création des JRadioButton
    // Taille de la MAP
    private static JRadioButton tailleMap9;
    private static JRadioButton tailleMap13;
	private static JRadioButton tailleMap19;
    // Handicaps
    private static JRadioButton handicaps0;
    private static JRadioButton handicaps1;
    private static JRadioButton handicaps2;
    private static JRadioButton handicaps3;
    private static JRadioButton handicaps4;
    private static JRadioButton handicaps5;
    private static JRadioButton handicaps6;
    private static JRadioButton handicaps7;
    private static JRadioButton handicaps8;
    private static JRadioButton handicaps9;
    // Horloge
    private static JRadioButton horlogeNone;
    private static JRadioButton horlogeAbsolue;
	private static JRadioButton horlogeByoYomi;

    // Groupes de boutons pour n'en sélectionner qu'un seul
    ButtonGroup groupeTailleMap = new ButtonGroup();
    ButtonGroup groupeHandicaps = new ButtonGroup();
    ButtonGroup groupeHorloge = new ButtonGroup();

    // TEXTE
    JLabel texteOptions;
    JLabel texteTailleMap;
    JLabel texteHandicaps;
    JLabel texteHorloge;

    // BOUTON DE LANCEMENT
    JButton buttonLancer;

    // CHOIX
    int choixTailleMap = 0;
    int choixHandicaps = 0;
    int choixHorloge = 0;

  	public PartiePerso() {
        // TEXTE
        texteTailleMap = new JLabel("Goban : ");
        Font fontTailleMap = new Font("Arial",Font.BOLD,20);
        texteTailleMap.setFont(fontTailleMap);

        texteHandicaps = new JLabel("Handicaps : ");
        Font fontHandicaps = new Font("Arial",Font.BOLD,20);
        texteHandicaps.setFont(fontHandicaps);

        texteHorloge = new JLabel("Horloge : ");
        Font fontHorloge = new Font("Arial",Font.BOLD,20);
        texteHorloge.setFont(fontHorloge);

        // BOUTONS
    	JLabel sautLigne = new JLabel("<html><br><br><br>");
    	sautLigne.setFont(new Font("Serif", Font.PLAIN, 20));
		JLabel sautLigne2 = new JLabel("<html><br><br><br>");
    	sautLigne2.setFont(new Font("Serif", Font.PLAIN, 20));
        JLabel sautLigne3 = new JLabel("<html><br><br><br>");
        sautLigne3.setFont(new Font("Serif", Font.PLAIN, 20));
        JLabel sautLigne4 = new JLabel("<html><br><br><br>");
        sautLigne4.setFont(new Font("Serif", Font.PLAIN, 20));

    	JPanel panelTailleMap = new JPanel();
    	//On définit le layout en lui indiquant qu'il travaillera en ligne
	    panelTailleMap.setLayout(new BoxLayout(panelTailleMap, BoxLayout.LINE_AXIS));

        JPanel panelHandicaps = new JPanel();
        panelHandicaps.setLayout(new BoxLayout(panelHandicaps, BoxLayout.LINE_AXIS));

        JPanel panelHorloge = new JPanel();
        panelHorloge.setLayout(new BoxLayout(panelHorloge, BoxLayout.LINE_AXIS));

        // Création des boutons de tailleMap
	    tailleMap9 = new JRadioButton(" 9x9 ");
        tailleMap13 = new JRadioButton(" 13x13 ");
	    tailleMap19 = new JRadioButton(" 19x19 ");
        // On les ajoutes au groupe pour n'en sélectionner qu'un seul à chaque fois
        groupeTailleMap.add(tailleMap9);
        groupeTailleMap.add(tailleMap13);
        groupeTailleMap.add(tailleMap19);
        // On les ajoutes au Panel de tailleMap
        panelTailleMap.add(texteTailleMap);
        panelTailleMap.add(tailleMap9);
        panelTailleMap.add(tailleMap13);
        panelTailleMap.add(tailleMap19);

        // Créations des boutons pour les handicaps :
        handicaps0 = new JRadioButton(" Aucun handicap ");
        handicaps1 = new JRadioButton(" 1 ");
        handicaps2 = new JRadioButton(" 2 ");
        handicaps3 = new JRadioButton(" 3 ");
        handicaps4 = new JRadioButton(" 4 ");
        handicaps5 = new JRadioButton(" 5 ");
        handicaps6 = new JRadioButton(" 6 ");
        handicaps7 = new JRadioButton(" 7 ");
        handicaps8 = new JRadioButton(" 8 ");
        handicaps9 = new JRadioButton(" 9 ");
        // On les ajoutes au groupe pour n'en sélectionner qu'un seul à chaque fois
        groupeHandicaps.add(handicaps0);
        groupeHandicaps.add(handicaps1);
        groupeHandicaps.add(handicaps2);
        groupeHandicaps.add(handicaps3);
        groupeHandicaps.add(handicaps4);
        groupeHandicaps.add(handicaps5);
        groupeHandicaps.add(handicaps6);
        groupeHandicaps.add(handicaps7);
        groupeHandicaps.add(handicaps8);
        groupeHandicaps.add(handicaps9);
        // On les ajoutes au Panel de handicaps
        panelHandicaps.add(texteHandicaps);
        panelHandicaps.add(handicaps0);
        panelHandicaps.add(handicaps1);
        panelHandicaps.add(handicaps2);
        panelHandicaps.add(handicaps3);
        panelHandicaps.add(handicaps4);
        panelHandicaps.add(handicaps5);
        panelHandicaps.add(handicaps6);
        panelHandicaps.add(handicaps7);
        panelHandicaps.add(handicaps8);
        panelHandicaps.add(handicaps9);

        // Création des boutons de tailleMap
        horlogeNone = new JRadioButton(" Aucune horloge ");
        horlogeAbsolue = new JRadioButton(" Horloge absolue - 30min ");
        horlogeByoYomi = new JRadioButton(" Mode Byo-Yomi - 30min puis 30s");
        // On les ajoutes au groupe pour n'en sélectionner qu'un seul à chaque fois
        groupeHorloge.add(horlogeNone);
        groupeHorloge.add(horlogeAbsolue);
        groupeHorloge.add(horlogeByoYomi);
        // On les ajoutes au Panel de tailleMap
        panelHorloge.add(texteHorloge);
        panelHorloge.add(horlogeNone);
        panelHorloge.add(horlogeAbsolue);
        panelHorloge.add(horlogeByoYomi);

        // Bouton pour lancer la partie
        buttonLancer = new JButton("Lancer la partie avec ces options");

	    JPanel panelGlobal = new JPanel();
	    //On positionne maintenant ces trois lignes en colonne
	    panelGlobal.add(panelTailleMap);
	    panelGlobal.add(sautLigne2);
	    panelGlobal.add(panelHandicaps);
        panelGlobal.add(sautLigne3);
        panelGlobal.add(panelHorloge);
        panelGlobal.add(sautLigne4);
        panelGlobal.add(buttonLancer);

        // Couleur de la fenêtre
	    Color backgroundColor = new Color(160, 82, 45);
	    panelGlobal.setBackground(backgroundColor);
        // Propriétés de la fenêtre
	    this.getContentPane().add(panelGlobal);
	   
	    // Action des boutons 
        tailleMap9.addActionListener(this);
        tailleMap13.addActionListener(this);
        tailleMap19.addActionListener(this);

        buttonLancer.addActionListener(this);

        // Boutons sélectionnés par défaut
        tailleMap9.setSelected(true);
        handicaps0.setSelected(true);
        horlogeAbsolue.setSelected(true);

	}

  	// Actions des boutons
    public void actionPerformed(ActionEvent e) {
        // Choisir la source du bouton (Passer ou Abandonner)
        Object source = e.getSource();

        // Taille de la MAP
        if(this.tailleMap9.isSelected()) choixTailleMap = 9;
        else if(this.tailleMap13.isSelected()) choixTailleMap = 13;
        else if(this.tailleMap19.isSelected()) choixTailleMap = 19;
        else choixTailleMap = 19;

        // Nombre de handicaps
        if(this.handicaps0.isSelected()) choixHandicaps = 0;
        else if(this.handicaps1.isSelected()) choixHandicaps = 1;
        else if(this.handicaps2.isSelected()) choixHandicaps = 2;
        else if(this.handicaps3.isSelected()) choixHandicaps = 3;
        else if(this.handicaps4.isSelected()) choixHandicaps = 4;
        else if(this.handicaps5.isSelected()) choixHandicaps = 5;
        else if(this.handicaps6.isSelected()) choixHandicaps = 6;
        else if(this.handicaps7.isSelected()) choixHandicaps = 7;
        else if(this.handicaps8.isSelected()) choixHandicaps = 8;
        else if(this.handicaps9.isSelected()) choixHandicaps = 9;
        else choixHandicaps = 0;

        // Horloge
        if(this.horlogeNone.isSelected()) choixHorloge = 0;
        else if(this.horlogeAbsolue.isSelected()) choixHorloge = 1;
        else if(this.horlogeByoYomi.isSelected()) choixHorloge = 2;
        else choixHorloge = 0;


        if (source == buttonLancer) {
            // System.out.println("Lancement de partie");

            // Création de la fenêtre
            JFrame fenetre = new JFrame();
            fenetre.setSize(new Dimension(1920, 1080));
            fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fenetre.setLayout(null);

            // Passage des paramètres
            Jeu jeu = new Jeu(choixTailleMap, choixHandicaps, choixHorloge);
            fenetre.setContentPane(jeu);
            
            fenetre.setVisible(true);

            // Fermer cette fenêtre
            this.dispose();
        }
    }
  
  	public static void main(String[] args) {
    	
  	}
}