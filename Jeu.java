import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.io.*;

public class Jeu extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

    // Définition de la map de base entre 9, 13 et 19
    int tailleMap = 19;
    int mapTailleCarreau = 40;

    // Variables pour la souris
    int mouseX = 0, mouseY = 0;
    int curseurX = 0;
    int curseurY = 0;

    // Tour du joueur
    int tourJoueur = 0;

    // Nombre de prisonniers
    int prisonnierJ1 = 0;
    int prisonnierJ2 = 0;

    // Temps restant aux joueurs
    int tmpRestantJ1 = 0;
    int tmpRestantJ2 = 0;

    // Transparence pour les couleurs
    int opacite25 = 64;     // 25% transparent
    int opacite50 = 127;    // 50% transparent
    int opacite75 = 19;     // 75% transparent

    // Initialisation de la MAP :
    // Si 0 : Aucun joueur n'a posé de pion
    // Si 1 : Le joueur 1 à posé un pion
    // Si 2 : Le joueur 2 à posé un pion

    int[][] tabMap;
    int[][] tabMapTer;

    // Création des groupes de pierres
    ArrayList<Groupe> groupe = new ArrayList<Groupe>();
    // Groupe mourrant
    Groupe deadGroupe = null;

    // Groupe pour l'historique
    ArrayList<Historique> historique = new ArrayList<Historique>();
    // Pointeur pour l'historique
    int ptHistorique = 0;

    int handicap = 0;
    // Si joueurHandicap = 1 : Joueur 1, si = 2, Joueur 2
    int joueurHandicap = 1;
    int passerTourHandicap = 0;
    int firstPassage = 0;         // On ne veut passer qu'une seule fois dans la boucle pour afficher les pierres

    // Si le joueur clique le bouton "Passer son tour"
    int passerTour = 0;

    // Score final des deux joueurs
    // Le J2 à des points d'avances grâce au komi (Joueur noir qui commence)
    double scoreTotalJoueur1 = 0;
    double scoreTotalJoueur2 = 7.5;

    // Savoir si on active le temps ? 
        // 0 : Pas de temps
        // 1 : Horloge absolue
        // 2 : Byo - Yomi
    int enableTime = 1;
    int tempsGlobal = 30;   // En minutes
    int tempsByoYomi = 30;  // En secondes

    long timeStart;
    long timeJ1 = tempsGlobal*60*1000;          // Temps global pour le joueur 1
    long timeJ2 = tempsGlobal*60*1000;          // Temps global pour le joueur 2
    long byoYomiJ1 = (tempsByoYomi*1000);       // Temps par tour si le temps global est épuisé pour le joueur 1
    long byoYomiJ2 = (tempsByoYomi*1000);       // Temps par tour si le temps global est épuisé pour le joueur 2

    int passageByoJ1 = 0;
    int passageByoJ2 = 0;

    long timeStartByoJ1 = 0;
    long timeStartByoJ2 = 0;

    long timeDeltaJ1;
    int rentrer = 1;

    // Parcourir les ArrayList : 
    // for(Groupe onegroupe: groupe)
    // = Pour tout les onegroupe de type Groupe du tableau groupe

    // Fin de partie
    int finPartie = 0;      // Si = 0 : La partie n'est pas finie.  Si = 1 : Partie terminée
    int finPartieAbandon = 0;      // Si = 0 : La partie n'est pas finie.  Si = 1 : Partie terminée

    // Variable pour savoir si la partie est finie
    int j1Gagne = 0;    // Affiche : Le joueur 1 gagne la partie et toutes les intéractions sont bloquées
    int j2Gagne = 0;    // Affiche : Le joueur 2 gagne la partie et toutes les intéractions sont bloquées
    int jEgalite = 0;   // Affiche : Egalité et ...

    private static JButton btnPass;
    private static JButton btnAbandon;
    private static JButton btnAnnuler;
    private static JButton btnDesannuler;
    private static JTextArea txtHistorique;
    private static JScrollPane scrollPane;

    public Jeu(int tailleMap, int handicap, int enableTime) {

        // DEBUG DEBUG
        // tailleMap = 3;

        // Récupération des variables en fonction des options
        this.tailleMap = tailleMap;
        this.handicap = handicap;
        this.enableTime = enableTime;

        // On crée la taille de 19x19
        tabMap = new int[tailleMap][tailleMap];
        tabMapTer = new int[tailleMap][tailleMap];
        
        // On défini une taille par défaut pour la taille de la grille
        //mapTailleCarreau = 800/tailleMap;

        // Obligatoire pour prendre les coordonnées de la souris
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Variable pour initialiser le temps de départ
        timeStart = System.currentTimeMillis();

        this.setLayout(null);

        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Si le joueur 'noir' possède des handicaps, alors c'est le joueur 2 qui commence
        if(handicap != 0)
            tourJoueur = 1;

        // Création des objets
        btnPass = new JButton("Passer");
        btnAbandon = new JButton("Abandonner");
        btnAnnuler = new JButton("Annuler");
        btnDesannuler = new JButton("Désannuler");
        txtHistorique = new JTextArea("Historique des coups : \n");
        scrollPane = new JScrollPane(txtHistorique);

        // Afficher les boutons
        // Positionner le bouton
        // txtHistorique.setBounds(970, 430, 420, 350);
        btnPass.setBounds(1435, 830, 200, 50);
        btnAbandon.setBounds(1190, 830, 200, 50);
        btnAnnuler.setBounds(1560, 500, 200, 50);
        btnDesannuler.setBounds(1560, 600, 200, 50);

        txtHistorique.setEditable(false);

        scrollPane.setBounds(970, 430, 420, 350);
        scrollPane.setPreferredSize(new Dimension(420, 350));
        this.add(scrollPane);

        this.add(btnPass);
        this.add(btnAbandon);
        this.add(btnAnnuler);
        this.add(btnDesannuler);

        this.setBackground(new Color(205, 133, 63));
        this.setVisible(true);

        // Action des boutons 
        btnPass.addActionListener(this);
        btnAbandon.addActionListener(this);
        btnAnnuler.addActionListener(this);
        btnDesannuler.addActionListener(this);

        // Première sauvegarde de la MAP
        Historique addHistorique = new Historique(tabMap, prisonnierJ1, prisonnierJ2, groupe, (tourJoueur+1)%2, -1, -1, timeJ1, timeJ2);    // On fais tourJoueur+1%2 pour sauvegarder le joueur qui ne commence pas
        historique.add(addHistorique);

        /*
        tabMapTer[3][0] = 2;
        tabMapTer[3][1] = 2;
        tabMapTer[2][2] = 2;
        tabMapTer[1][3] = 2;
        tabMapTer[0][4] = 2;

        System.out.println("###### DEBUT");
        
        //int retour = recursifTerritoires(0, tabMapTer, 0, 0, true);
    
        System.out.println("###### FIN : "+retour);
        */
    }

    // Actions des boutons
    public void actionPerformed(ActionEvent e) {
        // Choisir la source du bouton (Passer ou Abandonner)
        Object source = e.getSource();

        if(source == btnAnnuler) {
            if(ptHistorique > 1) {
                annulerCoup(false);
            }
        }

        if(source == btnDesannuler) {
            if(ptHistorique < historique.size()) {
                ptHistorique++;

                historique.get(ptHistorique-1).getHistorique(tabMap, groupe);
                prisonnierJ1 = historique.get(ptHistorique-1).getScoreJ1();
                prisonnierJ2 = historique.get(ptHistorique-1).getScoreJ2();
                tourJoueur = historique.get(ptHistorique-1).getTourJoueurSuivant();
                timeJ1 = historique.get(ptHistorique-1).getTimeJ1();
                timeJ2 = historique.get(ptHistorique-1).getTimeJ2();

                tourJoueur = (tourJoueur+1)%2;

                txtHistorique.append("RAJOUT # Pion en [" + historique.get(ptHistorique-1).getCooX() + ";" + historique.get(ptHistorique-1).getCooY() + "]\n");

                draw();
            }
        }

        if(passerTour == 0) {
            // Si on clique sur le bouton passer
            if(source == btnPass) {
                // Gestion du temps du joueur
                if(tourJoueur == 0)
                    timeJ1 = timeJ1 - (System.currentTimeMillis()-timeStart);
                else
                    timeJ2 = timeJ2 - (System.currentTimeMillis()-timeStart);

                timeStart = System.currentTimeMillis();

                tourJoueur = (tourJoueur+1)%2;
                passerTour = 1;
                // System.out.println("PASSER");
            }

            // Si on clique sur Abandonner
            else if(source == btnAbandon) {
                if(tourJoueur == 0) {
                    scoreTotalJoueur1 = scoreTotalJoueur1 + prisonnierJ1;
                    scoreTotalJoueur2 = scoreTotalJoueur2 + prisonnierJ2;

                    j2Gagne = 1;
                    finPartie = 1;
                    finPartieAbandon = 1;
                    javax.swing.JOptionPane.showMessageDialog(null,"Le joueur 2 remporte la partie !","Fin de partie",JOptionPane.WARNING_MESSAGE);
                } else {
                    scoreTotalJoueur1 = scoreTotalJoueur1 + prisonnierJ1;
                    scoreTotalJoueur2 = scoreTotalJoueur2 + prisonnierJ2;
                    
                    j1Gagne = 1;
                    finPartie = 1;
                    finPartieAbandon = 1;
                    javax.swing.JOptionPane.showMessageDialog(null,"Le joueur 1 remporte la partie !","Fin de partie",JOptionPane.WARNING_MESSAGE);
                }
                // System.out.println("ABANDON");
            }
        } else {
            // System.out.println("FIN DE LA PARTIE CAR DOUBLE PASSE");
            finPartie = 1;
            // On demande de supprimer les groupes morts
            javax.swing.JOptionPane.showMessageDialog(null,"Sélectionnez les groupes morts","Groupes morts",JOptionPane.WARNING_MESSAGE);
        }
    }

    public void annulerCoup(boolean ko) {
        ptHistorique--;

        historique.get(ptHistorique-1).getHistorique(tabMap, groupe);
        prisonnierJ1 = historique.get(ptHistorique-1).getScoreJ1();
        prisonnierJ2 = historique.get(ptHistorique-1).getScoreJ2();
        tourJoueur = historique.get(ptHistorique-1).getTourJoueurSuivant();
        timeJ1 = historique.get(ptHistorique-1).getTimeJ1();
        timeJ2 = historique.get(ptHistorique-1).getTimeJ2();

        if(!ko)
            txtHistorique.append("ANNULATION # Pion en [" + historique.get(ptHistorique).getCooX() + ";" + historique.get(ptHistorique).getCooY() + "]\n");

        draw();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Initialisation des graphismes
        Graphics2D g2d = (Graphics2D) g;

        // ===== Fond gris affichages ====== //
        g2d.setColor(Color.GRAY);
        // Haut
        g2d.fillRect(950, 0, 980, 430);
        // Gauche historique
        g2d.fillRect(950, 0, 20, 850);
        // Droite historique en haut
        g2d.fillRect(1390, 0, 550, 500);
        // Droite historique gauche
        g2d.fillRect(1390, 0, 170, 800);
        // Droite historique droite
        g2d.fillRect(1760, 0, 200, 800);
        // Droite historique milieu
        g2d.fillRect(1560, 550, 200, 50);
        // Droite historique bas
        g2d.fillRect(1560, 650, 200, 150);
        // Sous historique
        g2d.fillRect(950, 780, 1000, 50);
        // Sous les boutons
        g2d.fillRect(950, 880, 980, 200);
        // Gauche
        g2d.fillRect(950, 830, 240, 60);
        // Milieu
        g2d.fillRect(1390, 830, 45, 200);
        // Droite
        g2d.fillRect(1635, 830, 300, 200);
        // ================================= //        

        // Trait de séparation
        g2d.setColor(Color.BLACK);
        g2d.fillRect(950, 0, 1, 1000);

        // Réglages en fonction de la taille de la MAP
        int incHoshi = 0;
        int depHoshi = 4;

        if(tailleMap == 9) {
            incHoshi = 2;
            depHoshi = 3;
            mapTailleCarreau = 90;

        } else if(tailleMap == 13) {
            incHoshi = 3;
            mapTailleCarreau = 70;

        } else if(tailleMap == 19)
            incHoshi = 6;

        // Permet de rétrécir la fenetre
        if(tailleMap == 19)
            mapTailleCarreau = (getHeight() < getWidth() ? getHeight() : getWidth()) / 20;
        else if(tailleMap == 13)
            mapTailleCarreau = (getHeight() < getWidth() ? getHeight() : getWidth()) / 15;
        else if(tailleMap == 9)
            mapTailleCarreau = (getHeight() < getWidth() ? getHeight() : getWidth()) / 10;

        // On affiche la grille en fonction de la taille de la MAP
        for(int i = 0; i < tailleMap-1; i++) {
            for(int j = 0; j < tailleMap-1; j++) {
                g2d.setColor(Color.BLACK);
                g2d.drawRect(mapTailleCarreau+mapTailleCarreau*i, mapTailleCarreau+mapTailleCarreau*j, mapTailleCarreau, mapTailleCarreau);
            }
        }

        // Hoshi (Points noirs)
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(!(((i == 1 && j == 0) || (i == 0 && j == 1) || (i == 2 && j == 1) || (i == 1 && j == 2)) && tailleMap != 19))
                    g2d.fillOval(((depHoshi+i*incHoshi)*mapTailleCarreau)-(mapTailleCarreau/8), ((depHoshi+j*incHoshi)*mapTailleCarreau)-(mapTailleCarreau/8), mapTailleCarreau/4, mapTailleCarreau/4);
            }
        }

        // Si la souris est sur le plateau : On affiche le curseur
        if(finPartie == 0) {        // Si la partie est finie, on n'affiche plus le carré blanc
            if(curseurX >= 0 && curseurX < tailleMap && curseurY >= 0 && curseurY < tailleMap) {
                // Blanc transparente
                Color couleurSelecteur = new Color(255, 255, 255, opacite50);
                g2d.setColor(couleurSelecteur);

                g2d.fillRect(((curseurX+1)*mapTailleCarreau)-(mapTailleCarreau/2), ((curseurY+1)*mapTailleCarreau)-(mapTailleCarreau/2), mapTailleCarreau, mapTailleCarreau);
                g2d.setColor(Color.BLACK);
                // Affichage des coordonnées de la souris en haut à gauche
                String coordonneesAffichage = String.format("%d, %d", curseurX, curseurY);
                g2d.drawString(coordonneesAffichage, 20, 20);
            }
        }

        // On affiche le pion  des joueurs
        for(int i = 0; i < tailleMap; i++) {
            for(int j = 0; j < tailleMap; j++) {
                if(tabMap[i][j] > 0) {
                    // On change la couleur en fonction des joueurs
                    if (tabMap[i][j] == 1)
                        g2d.setColor(Color.BLACK);
                    if (tabMap[i][j] == 2)
                        g2d.setColor(Color.WHITE);
                    g2d.fillOval(mapTailleCarreau+(i*mapTailleCarreau)-(mapTailleCarreau/2), mapTailleCarreau+(j*mapTailleCarreau)-(mapTailleCarreau/2), mapTailleCarreau, mapTailleCarreau);
                }
            }
        }

        // Handicaps :
        if(handicap >= 2 && firstPassage == 0) {
            passerTourHandicap = 1;
            firstPassage = 1;
            if(tailleMap == 9) {
                tabMap[2][6] = joueurHandicap;
                groupe.add(new Groupe(2, 6, joueurHandicap));
                tabMap[6][2] = joueurHandicap;
                groupe.add(new Groupe(6, 2, joueurHandicap));
            } else {
                tabMap[3][(tailleMap-4)] = joueurHandicap;
                groupe.add(new Groupe(3, (tailleMap-4), joueurHandicap));
                tabMap[(tailleMap-4)][3] = joueurHandicap;
                groupe.add(new Groupe((tailleMap-4), 3, joueurHandicap));
            }
            if(handicap >= 3) {
                if(tailleMap == 9) {
                    if(joueurHandicap == 1) {
                        tabMap[2][2] = joueurHandicap;
                        groupe.add(new Groupe(2, 2, joueurHandicap));
                    } else {
                        tabMap[(tailleMap-4)][(tailleMap-4)] = joueurHandicap;
                        groupe.add(new Groupe((tailleMap-4), (tailleMap-4), joueurHandicap));
                    }
                } else {
                    if(joueurHandicap == 1) {
                        tabMap[3][3] = joueurHandicap;
                        groupe.add(new Groupe(3, 3, joueurHandicap));
                    } else {
                        tabMap[(tailleMap-4)][(tailleMap-4)] = joueurHandicap;
                        groupe.add(new Groupe((tailleMap-4), (tailleMap-4), joueurHandicap));
                    }

                }
                if(handicap >= 4) {
                    if(tailleMap == 9) {
                        if(joueurHandicap == 1) {
                            tabMap[(tailleMap-3)][(tailleMap-3)] = joueurHandicap;
                            groupe.add(new Groupe((tailleMap-3), (tailleMap-3), joueurHandicap));
                        }
                        else {
                            tabMap[2][2] = joueurHandicap;
                            groupe.add(new Groupe(2, 2, joueurHandicap));
                        }
                    } else {
                        if(joueurHandicap == 1) {
                            tabMap[(tailleMap-4)][(tailleMap-4)] = joueurHandicap;
                            groupe.add(new Groupe((tailleMap-4), (tailleMap-4), joueurHandicap));
                        } else {
                            tabMap[3][3] = joueurHandicap;
                            groupe.add(new Groupe(3, 3, joueurHandicap));
                        }
                    }
                    if(handicap == 5) {
                        if(tailleMap == 19) {
                            tabMap[9][9] = joueurHandicap;
                            groupe.add(new Groupe(9, 9, joueurHandicap));
                        } else if(tailleMap == 13) {
                            tabMap[6][6] = joueurHandicap;
                            groupe.add(new Groupe(6, 6, joueurHandicap));
                        } else {
                            tabMap[4][4] = joueurHandicap;
                            groupe.add(new Groupe(4, 4, joueurHandicap));
                        }
                    }
                    if(tailleMap == 19) {
                        if(handicap >= 6) {
                            tabMap[3][9] = joueurHandicap;
                            groupe.add(new Groupe(3, 9, joueurHandicap));
                            tabMap[15][9] = joueurHandicap;
                            groupe.add(new Groupe(15, 9, joueurHandicap));
                            if(handicap == 7)
                                tabMap[9][9] = joueurHandicap;
                                groupe.add(new Groupe(9, 9, joueurHandicap));
                            if(handicap >= 8) {
                                tabMap[9][15] = joueurHandicap;
                                groupe.add(new Groupe(9, 15, joueurHandicap));
                                tabMap[9][3] = joueurHandicap;
                                groupe.add(new Groupe(9, 3, joueurHandicap));
                                if(handicap == 9)
                                    tabMap[9][9] = joueurHandicap;
                                    groupe.add(new Groupe(9, 9, joueurHandicap));
                            }
                        } 
                    }
                }
            }
        }

        // Le joueur blanc commence si le joueur noir à un handicap
        if(passerTourHandicap == 1 && joueurHandicap == 1) {
            //tourJoueur = (tourJoueur+1)%2;
            passerTourHandicap = 2;
        }

        // ---------- Affichages ----------
        // Contour zone d'affichage
        // Color couleurZoneJoueur = new Color(0, 0, 0, opacite25);
        // g2d.setColor(couleurZoneJoueur);
        // g2d.fillRect(mapTailleCarreau*20, mapTailleCarreau, 940, 850);
        g2d.setColor(Color.BLACK);
        // Titre
        String titreAffichage = String.format("Jeu de GO");
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
        g2d.drawString(titreAffichage, 1265, 100);

        // En fonction de qui gagne
        if(j1Gagne == 0 && j2Gagne == 0) {

            // Tour du joueur
            String tourDeJouerAffichage = String.format("C'est au tour du joueur %d", ((tourJoueur+1)));
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
            g2d.drawString(tourDeJouerAffichage, 1100, 200);

        } else if (j1Gagne == 1 && j2Gagne == 0) {
            // Affichage du gagnant
            String affichageGagnant = String.format(" Le joueur 1 gagne la partie !");
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
            g2d.drawString(affichageGagnant, 1100, 200);

        } else {
            // Affichage du gagnant
            String affichageGagnant = String.format(" Le joueur 2 gagne la partie !");
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
            g2d.drawString(affichageGagnant, 1100, 200);
        }

        // Séparation entre les 2 joueurs
        g2d.drawRect(1412, 250, 0, 160);
        // Historique
        int numGroupe = 0;
        int posJ1 = 0;
        int posJ2 = 0;

        // Joueur 1
        String joueur1Affichage = String.format("- Joueur 1 (Pions noirs) -");
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
        g2d.drawString(joueur1Affichage, 990, 285);
        // Joueur 2
        String joueur2Affichage = String.format("- Joueur 2 (Pions blancs) -");
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
        g2d.drawString(joueur2Affichage, 1470, 285);

            
        if(finPartieAbandon == 0) {
            if(finPartie == 0) {    // Si la partie est terminée, on affiche le score total
                // Pions capturés par le J1
                String pionJ1Affichage = String.format("- Vous avez capturé %d pions.", prisonnierJ1);
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
                g2d.drawString(pionJ1Affichage, 1020, 350);
            } else {
                String pionJ1Affichage = String.format("- Vous avez "+scoreTotalJoueur1+" points.");
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
                g2d.drawString(pionJ1Affichage, 1020, 350);
            }

            if(finPartie == 0) {    // Si la partie est terminée, on affiche le score total
                // Pions capturés par le J2
                String pionJ2Affichage = String.format("- Vous avez capturé %d pions.", prisonnierJ2);
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
                g2d.drawString(pionJ2Affichage, 1500, 350);
            } else {
                String pionJ2Affichage = String.format("- Vous avez "+scoreTotalJoueur2+" points.");
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
                g2d.drawString(pionJ2Affichage, 1500, 350);
            }
        }

        // ---------- TEMPS ----------
        
        String tmpJ1Affichage;
        String tmpJ2Affichage;

        // Si on utilise l'horloge absolue
        if(enableTime == 1) {
            long timeDelta = System.currentTimeMillis() - timeStart;

            // Joueur 1
            long timePrintJ1 = timeJ1 - timeDelta;
            if(tourJoueur != 0) {
                timePrintJ1 = timeJ1;
            }

            // Si le temps est égal à 0, le joueur 2 gagne
            if(TimeUnit.MILLISECONDS.toMinutes(timePrintJ1) <= 0 && TimeUnit.MILLISECONDS.toSeconds(timePrintJ1%(1000*60)) <= 0) {
                j2Gagne = 1;
            }

            if(finPartie == 0) {        // Si la partie est terminée, le temps s'arrête
                tmpJ1Affichage = String.format("- Temps restant : %02d:%02d", 
                    TimeUnit.MILLISECONDS.toMinutes(timePrintJ1), 
                    TimeUnit.MILLISECONDS.toSeconds(timePrintJ1%(1000*60)));
            } else {
                tmpJ1Affichage = String.format("- Partie terminée !");
            }

            // Joueur 1
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
            g2d.drawString(tmpJ1Affichage, 1020, 400);

            // Joueur 2
            long timePrintJ2 = timeJ2 - timeDelta;
            if(tourJoueur != 1) {
                timePrintJ2 = timeJ2;
            }

            // Si le temps est égal à 0, le joueur 1 gagne
            if(TimeUnit.MILLISECONDS.toMinutes(timePrintJ2) <= 0 && TimeUnit.MILLISECONDS.toSeconds(timePrintJ2%(1000*60)) <= 0) {
                j1Gagne = 1;
            }

            if(finPartie == 0) {
                tmpJ2Affichage = String.format("- Temps restant : %02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(timePrintJ2), 
                    TimeUnit.MILLISECONDS.toSeconds(timePrintJ2%(1000*60)));
            } else {
                tmpJ2Affichage = String.format("- Partie terminée !");
            }

            // Joueur 2
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
            g2d.drawString(tmpJ2Affichage, 1500, 400);


        // Si on utilise le mode Byo-Yomi
        } else if (enableTime == 2) {

            long timeDelta = System.currentTimeMillis()-timeStart;

            // ===== Joueur 1 =====
            long timePrintJ1 = timeJ1 - timeDelta;
            if(tourJoueur != 0)
                timePrintJ1 = timeJ1;

            // ===== Joueur 2 =====
            long timePrintJ2 = timeJ2 - timeDelta;
            if(tourJoueur != 1)
                timePrintJ2 = timeJ2;

            // Si le joueur 1 n'a plus de temps, on passe en mode Byo Yomi
            if(TimeUnit.MILLISECONDS.toMinutes(timePrintJ1) <= 0 && TimeUnit.MILLISECONDS.toSeconds(timePrintJ1%(1000*60)) <= 0) {
                if(timeStartByoJ1 == 0)
                    timeStartByoJ1 = System.currentTimeMillis();

                long timeDeltaByoJ1 = System.currentTimeMillis() - timeStartByoJ1;

                long timePrintJ1Byo = byoYomiJ1 - timeDeltaByoJ1;

                if(tourJoueur != 0) {
                    timePrintJ1Byo = byoYomiJ1;
                }

                // Affichage J1
                String tmpJ1AffichageByo = String.format("- Temps restant : %02d:%02d - Byo Yomi", 
                    0, 
                    TimeUnit.MILLISECONDS.toSeconds(timePrintJ1Byo%(1000*60)));

                // Joueur 1
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
                g2d.drawString(tmpJ1AffichageByo, 1020, 400);

            // Le joueur n'est pas encore en mode Byo-Yomi
            } else {
                // Affichage J1
                tmpJ1Affichage = String.format("- Temps restant : %02d:%02d", 
                    TimeUnit.MILLISECONDS.toMinutes(timePrintJ1), 
                    TimeUnit.MILLISECONDS.toSeconds(timePrintJ1%(1000*60)));

                // Joueur 1
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
                g2d.drawString(tmpJ1Affichage, 1020, 400);
            }

            // Si le joueur 2 n'a plus de temps, on passe en mode Byo Yomi
            if(TimeUnit.MILLISECONDS.toMinutes(timePrintJ2) <= 0 && TimeUnit.MILLISECONDS.toSeconds(timePrintJ2%(1000*60)) <= 0) {
                if(timeStartByoJ2 == 0)
                    timeStartByoJ2 = System.currentTimeMillis();

                long timeDeltaByoJ2 = System.currentTimeMillis() - timeStartByoJ2;

                long timePrintJ2Byo = byoYomiJ2 - timeDeltaByoJ2;

                if(tourJoueur != 1) {
                    timePrintJ2Byo = byoYomiJ2;
                }

                // Affichage J2
                String tmpJ2AffichageByo = String.format("- Temps restant : %02d:%02d - Byo Yomi", 
                    0, 
                    TimeUnit.MILLISECONDS.toSeconds(timePrintJ2Byo%(1000*60)));

                // Joueur 2
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
                g2d.drawString(tmpJ2AffichageByo, 1500, 400);

            // Le joueur n'est pas encore en mode Byo-Yomi
            } else {
                // Affichage J2
                tmpJ2Affichage = String.format("- Temps restant : %02d:%02d", 
                    TimeUnit.MILLISECONDS.toMinutes(timePrintJ2), 
                    TimeUnit.MILLISECONDS.toSeconds(timePrintJ2%(1000*60)));

                // Joueur 2
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
                g2d.drawString(tmpJ2Affichage, 1500, 400);
            }
        }
        // ------------------------------

        draw();
    }

    // Repaint
    public void draw(){
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {}

    // Obligatoire pour les interactions avec la souris
    @Override
    public void mouseEntered(MouseEvent arg0) {}

    @Override
    public void mouseExited(MouseEvent arg0) {}

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        boolean pionPose = false;       // Test si on pose un pion
        boolean isKO = false;           // Test si on est en situation de KO

        // Si un des joueurs à gagné, on arrête de jouer
        if(finPartie == 0) {        // Fin puis calcul de score
            if(j1Gagne == 0 && j2Gagne == 0) {      // Fin si abandon

                // Si on est bien dans les coordonnées du tableau
                if(!(curseurX > (tailleMap-1) || curseurY > (tailleMap-1) || curseurY < 0 || curseurX < 0)) {

                    // System.out.println("###### MouseClick");

                    if(tabMap[curseurX][curseurY] == 1 || tabMap[curseurX][curseurY] == 2) {
                        // Si il y a déjà une pierre, on ne fait rien
                        // System.out.println("Il y a déjà un pion sur cet emplacement !!");
                    } else {
                        passerTour = 0;
                        // System.out.println("X = "+curseurX+" # Y = "+curseurY);

                        // Propriétés d'un groupe
                        Groupe affectGroupe = null;
                        Groupe deleteGroupe = null;

                        // Vérification des voisins | Ajout au groupe
                        for(int i = 0; i < 4; i++) {

                            int voisinX = 0;
                            int voisinY = 0;
                            
                            // System.out.println("Voisin = "+i);
                            // Affecter voisin
                            if(i == 0) {                    // Haut
                                voisinX = curseurX;
                                voisinY = curseurY-1;
                            } else if(i == 1) {             // Droite
                                voisinX = curseurX+1;
                                voisinY = curseurY;
                            } else if(i == 2) {             // Bas
                                voisinX = curseurX;
                                voisinY = curseurY+1;                        
                            } else if(i == 3) {             // Gauche
                                voisinX = curseurX-1;
                                voisinY = curseurY;
                            }

                            // Si on est au bord, on ne fait rien (!)
                            if(!((voisinX < 0 && i == 3) || (voisinY < 0 && i == 0) || (voisinX >= tailleMap && i == 1) || (voisinY >= tailleMap && i == 2))) {

                                // Vérifier si voisin en fonction du joueur qui joue // si joueur identique au voisin, ajouter au groupe
                                if(((tabMap[voisinX][voisinY] == 1 && (tourJoueur+1) == 1) || (tabMap[voisinX][voisinY] == 2 && (tourJoueur+1) == 2))) {
                                    
                                    int groupeNumero = 0;
                                    Boolean check = false;

                                    // Parcourir tous les groupes
                                    for(Groupe oneGroupe:groupe) {

                                        if(!(affectGroupe == oneGroupe)) {

                                            // System.out.println("# Groupe = " + groupeNumero);
                                            groupeNumero++;

                                            // Vérifier si le voisin appartient au groupe
                                            if(oneGroupe.FindPierre(voisinX, voisinY) && check == false) {
                                                // System.out.println("    Pierre trouvée...");
                                                check = true;

                                                // Est ce qu'on a déjà affecté la pierre une fois ?
                                                if(affectGroupe == null) {
                                                    // System.out.println("         Insertion dans le groupe");

                                                    // Ajouter une pierre au groupe
                                                    oneGroupe.AddPierre(curseurX, curseurY);
                                                    affectGroupe = oneGroupe;

                                                } else {
                                                    // System.out.println("        Transfert du groupe");

                                                    // Transférer pierre de oneGroupe dans AffectGroup
                                                    for(Pierre onePierre:oneGroupe.ListePierre()) {
                                                        // System.out.println("            AddPierre : "+onePierre.getX()+", "+onePierre.getY());
                                                        affectGroupe.AddPierre(onePierre.getX(), onePierre.getY());
                                                    }

                                                    deleteGroupe = oneGroupe;
                                                }
                                            }
                                        }
                                    }
                                    // On supprime le groupe vide
                                    if (deleteGroupe != null) {
                                        groupe.remove(deleteGroupe); 
                                        deleteGroupe = null;                   
                                    }
                                }
                            }
                        }

                        if(affectGroupe == null) {
                            affectGroupe = new Groupe(curseurX, curseurY, tourJoueur);
                            // System.out.println("affectGroupe = " + affectGroupe);
                            // Récupération du groupe
                            groupe.add(affectGroupe);
                        }


                        tabMap[curseurX][curseurY] = tourJoueur + 1;

                        // Vérification de la mort d'un groupe pour le suicide
                        deadGroupe = null;
                        int ptJ1 = 0;
                        int ptJ2 = 0;

                        for(Groupe oneGroupe:groupe) {
                            // Si aucune liberté : On supprime le groupe et on donne les point à l'adversaire
                            // Puis on supprime le groupe
                            if(oneGroupe.Libertes(tabMap, tailleMap) == false && oneGroupe != affectGroupe) {
                                // On place une variable pour indiquer que le joueur mange, et donc qu'il peut jouer
                                if (tourJoueur == 1)
                                    ptJ2 = ptJ2 + oneGroupe.mortGroupe(tabMap);
                                else
                                    ptJ1 = ptJ1 + oneGroupe.mortGroupe(tabMap);
                                deadGroupe = oneGroupe;
                            }
                        }

                        // SUICIDE :
                        // SI LE GROUPE DANS LEQUEL ON AJOUTE LA PIERRE OBTIENT 0 LIBERTES ET PAS DE CAPTURE :
                        boolean testSuicide = !(affectGroupe.Libertes(tabMap, tailleMap));
                        if (testSuicide) {
                            // ON NE FAIT RIEN
                            tabMap[curseurX][curseurY] = 0;
                        // SINON
                        } else {
                            prisonnierJ1 += ptJ1;
                            prisonnierJ2 += ptJ2;
                            // On affiche dans l'historique
                            pionPose = true;
                            // System.out.println("Ecriture : "+txtHistorique.getX()+", "+txtHistorique.getY());
                        }

                        // Si le groupe est mort, on supprime ce groupe
                        if(deadGroupe != null) {
                            // System.out.println("deadGroupe = "+deadGroupe);
                            groupe.remove(deadGroupe);
                        }

                        // Gestion du temps du joueur
                        if(tourJoueur == 0)
                            timeJ1 = timeJ1 - (System.currentTimeMillis()-timeStart);
                        else
                            timeJ2 = timeJ2 - (System.currentTimeMillis()-timeStart);

                        timeStart = System.currentTimeMillis();

                        // System.out.println("Historique size = " + historique.size());
                        // System.out.println("ptHistorique = " + ptHistorique);

                        // Effacer la fin de la liste si on rejoue
                        while(historique.size() > ptHistorique && ptHistorique > 0) {
                            historique.remove(historique.size()-1);
                            // System.out.println("REMOVE");
                        }

                        Historique addHistorique = new Historique(tabMap, prisonnierJ1, prisonnierJ2, groupe, tourJoueur, curseurX, curseurY, timeJ1, timeJ2);
                        historique.add(addHistorique);

                        // On incrémente la variable pour annuler
                        ptHistorique = historique.size();

                        // On remet les groupes comme ils étaient auparavant
                        int nombreGroupe = 0;
                        for(Groupe thisGroupe : groupe) {
                            nombreGroupe++;
                        }

                        // System.out.println("Nombre de groupes : " + nombreGroupe);
                        // System.out.println("Historique = " + historique.size());
                        // System.out.println(Runtime.getRuntime().freeMemory()+" / "+Runtime.getRuntime().totalMemory());

                        // KO : Si on retombe sur une configuration déjà trouvée, alors on ne peut pas jouer
                        if(ptHistorique > 2) {
                            if(historique.get(ptHistorique-3).mapIdentiques(tabMap)) {
                                // On annule le dernier coup
                                annulerCoup(true);
                                tourJoueur = (tourJoueur+1)%2;
                                isKO = true;
                                // System.out.println("Situation de KO !");
                            }
                        }

                        // Test du triple KO
                        int nbIdentique = 0;
                        for(Historique thisHistorique : historique) {
                            if(thisHistorique.mapIdentiques(tabMap))
                                nbIdentique++;
                        }

                        if(nbIdentique >= 3) {
                            annulerCoup(true);
                            tourJoueur = (tourJoueur+1)%2;
                            isKO = true;
                        }

                        if(pionPose == true && isKO == false) {
                            txtHistorique.append("Joueur " + (tourJoueur + 1) + " # Pion en [" + curseurX + ";" + curseurY + "]\n");
                            txtHistorique.setCaretPosition(txtHistorique.getDocument().getLength());
                        }

                        // Si suicide, on ne change pas de joueur
                        if(!testSuicide) {
                            // Changement de joueur
                            tourJoueur = (tourJoueur+1)%2;

                            if(timeStartByoJ1 != 0)
                                timeStartByoJ1 = System.currentTimeMillis();
                            
                            if(timeStartByoJ2 != 0)
                                timeStartByoJ2 = System.currentTimeMillis();
                        }
                    } 
                }
            }
        // -- Dans le cas où la partie est terminée
        } else {
            // Si on est bien dans les coordonnées du tableau
            if(!(curseurX > (tailleMap-1) || curseurY > (tailleMap-1) || curseurY < 0 || curseurX < 0)) {

                System.out.println("Passage groupes morts");
                // On cherche les groupes morts
                if(tabMap[curseurX][curseurY] != 0) {

                    // MESSAGES BOX POUR LES GROUPES MORTS
                    // On sélectionne un groupe mort
                    // Confirmation 
                    Object[] options = {"Oui","Non"};
                    int isMort = JOptionPane.showOptionDialog(this,
                    "Êtes vous d'accord ?",
                    "Groupes morts",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,           //do not use a custom Icon
                    options,        //the titles of buttons
                    options[0]);       //default button title

                    if(isMort == 0) {
                        Groupe groupeMort = null;   // Groupe mort qui sera supprimé par la suite
                        // On trouve le groupe
                        for(Groupe thisGroupe : groupe) {
                            if(thisGroupe.FindPierre(curseurX, curseurY)) {
                                // On ajoute les points au joueur
                                /*
                                if(thisGroupe.getGroupe() == 0) {
                                    prisonnierJ1 = prisonnierJ1 - thisGroupe.mortGroupe(tabMap);
                                } else if(thisGroupe.getGroupe() == 1) {
                                    prisonnierJ2 = prisonnierJ2 - thisGroupe.mortGroupe(tabMap);
                                }
                                */

                                // On supprime le groupe mort
                                thisGroupe.mortGroupe(tabMap);
                                groupeMort = thisGroupe;
                                draw();
                            }
                        }                    
                        // On retire le groupe
                        groupe.remove(groupeMort);

                        // On peut alors sélectionner de nouveau les groupes morts
                        Object[] optionsContinuer = {"Oui","Non"};
                        int continuerGrpMort = JOptionPane.showOptionDialog(this,
                        "Tout les groupes sont morts ?",
                        "Fin ?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, 
                        optionsContinuer, 
                        optionsContinuer[0]); 

                        if(continuerGrpMort == 0) {
                            // On lance la fonction finale qui va chercher les groupes morts et les territoires
                            finPartie();
                            // On additionne les scores
                            scoreTotalJoueur1 = scoreTotalJoueur1 + prisonnierJ1;
                            scoreTotalJoueur2 = scoreTotalJoueur2 + prisonnierJ2;
                            // On affiche le vainqueur en fonction du score
                            affichageVainqueur(scoreTotalJoueur1, scoreTotalJoueur2);
                        }

                    } else {
                        // Le groupe n'est pas mort et la partie continue
                        javax.swing.JOptionPane.showMessageDialog(null,"Les joueurs ne sont pas d'accord\nLa partie continue...","Reprise",JOptionPane.WARNING_MESSAGE);
                        finPartie = 0;
                    }
                } else {
                    System.out.println("Il n'y a rien ici !");
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        mouseX = arg0.getX();
        mouseY = arg0.getY();

        curseurX = ((mouseX+(mapTailleCarreau/2))/mapTailleCarreau)-1;
        curseurY = ((mouseY+(mapTailleCarreau/2))/mapTailleCarreau)-1;      
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        mouseX = arg0.getX();
        mouseY = arg0.getY();

        curseurX = ((mouseX+(mapTailleCarreau/2))/mapTailleCarreau)-1;
        curseurY = ((mouseY+(mapTailleCarreau/2))/mapTailleCarreau)-1;
    }

    // A la fin de la partie UNIQUEMENT, calcul du score :
    public void finPartie() {
        // On passe une variable pour ne plus poser de pierres
        finPartie = 1;

        // Appel de la fonction des territoires :
        calculTerritoires(tabMap, tabMapTer);

        // FIN DE LA PARTIE : Calcul des scores avec les pierres sur le nouveau terrain avec les territoires
        for(int k = 0; k < tailleMap; k++) {
            for(int l = 0; l < tailleMap; l++) {
                if(tabMapTer[k][l] == 1) {
                    prisonnierJ1++;
                } else if (tabMapTer[k][l] == 2) {
                    prisonnierJ2++;
                }
            }
        }
    }

    private void calculTerritoires(int[][] pTabMap, int[][] pTabMapTer) {
        tabMap = pTabMap;
        tabMapTer = pTabMapTer;

        // Création de la nouvelle Map grâce à la première
        for(int k = 0; k < tailleMap; k++) {
            for(int l = 0; l < tailleMap; l++) {
                tabMapTer[k][l] = tabMap[k][l];
            }
        }

        for(int y = 0; y < tailleMap; y++) {
            for(int x = 0; x < tailleMap; x++) {
                int valeur = recursifTerritoires(0, tabMapTer, x, y, false);

                // if(x == 3 && y == 2)
                //     System.out.println("###### Retour = "+valeur);

                if(valeur == 1 || valeur == 2 || valeur == -1) {
                    int compteur = 0;
                    for(int k = 0; k < tailleMap; k++) {
                        for(int l = 0; l < tailleMap; l++) {
                            if(tabMapTer[l][k] == 3) {
                                tabMapTer[l][k] = valeur;
                                compteur++;
                            }
                        }
                    }

                    if(compteur > 0)
                        System.out.println("Joueur "+valeur+" + "+compteur+" point(s)");

                }
            }
        }
    }

    private boolean isBord(int x, int y, int tailleMap) {
        return(x < 0 || y < 0 || x > (tailleMap-1) || y > (tailleMap-1));
    }  

    private void printNiveau (int niveau, String chaine) {
        /*
        for (int j = 0 ; j < niveau ; j++) 
            System.out.print(" | "); 
        System.out.println (chaine);
        */
    }

    private int recursifTerritoires(int niveau, int[][] tabMapTer, int x, int y, boolean debug) {
        int retour = -1;

        printNiveau(niveau, "###### niveau [" + niveau + "] : "+x+", "+y);

        int tailleMap = tabMapTer[0].length;    // Taille du tableau

        if(isBord(x, y, tailleMap)) {
            printNiveau(niveau, "RETURN => 3");
            return 3;   // Bord
        }

        if(tabMapTer[x][y] > 0) retour = tabMapTer[x][y];

        if(tabMapTer[x][y] == 0) {
            
            // On doit regarder tout les côtés
            int valeur = -1;
            tabMapTer[x][y] = 3;
            // On appelle récursivement la fonction pour chaque pierre autour de celle qu'on étudie
            retour = recursifTerritoires(niveau+1, tabMapTer, x, y-1, debug);                     // Haut
            
            for(int i = 0; i < 3; i++) {
                if(i == 0)  valeur = recursifTerritoires(niveau+1, tabMapTer, x+1, y, debug);     // Droite
                if(i == 1)  valeur = recursifTerritoires(niveau+1, tabMapTer, x, y+1, debug);     // Bas
                if(i == 2)  valeur = recursifTerritoires(niveau+1, tabMapTer, x-1, y, debug);     // Gauche

                int oldRetour = retour;
                

                // Valeur de retour pour savoir si le groupe appartient à quelqu'un
                if(retour == -1 || valeur == -1)
                    retour = -1;
                else if(valeur != retour && valeur != 3 && retour != 3)
                    retour = -1;
                else if(retour == 3) {
                    retour = valeur;
                }
                printNiveau(niveau, "*" + i + " retour , valeur = " + oldRetour + ", " + valeur + " : retour = " + retour);
            }
        }
        printNiveau (niveau, "RETURN => " + retour);
        return retour;
    }

    public void affichageVainqueur(double scJ1, double scJ2) {
        double scoreJ1 = scJ1;
        double scoreJ2 = scJ2;

        if(scoreJ1 > scoreJ2) { 
            j1Gagne = 1;
            javax.swing.JOptionPane.showMessageDialog(null,"Le joueur 1 remporte la partie !","Fin de partie",JOptionPane.WARNING_MESSAGE);
        } else if(scoreJ1 < scoreJ2) {
            j2Gagne = 1;
            javax.swing.JOptionPane.showMessageDialog(null,"Le joueur 2 remporte la partie !","Fin de partie",JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {}
}