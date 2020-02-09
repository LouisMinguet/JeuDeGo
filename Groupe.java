import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Groupe {
    int joueur;

    // Tableau pour mettre les pierres
    ArrayList<Pierre> pierre = new ArrayList<Pierre>();

    // Permet de définir un groupe
    public Groupe (int px, int py, int pJoueur) {
        joueur = pJoueur;
        AddPierre(px, py);
    }

    int getGroupe() {
        return joueur;
    }

    // Permet d'ajouter une pierre à un groupe
    public void AddPierre(int px, int py) {
        pierre.add(new Pierre(px, py));
    }

    // Permet de regarder si une pierre appartient à un groupe. Si oui, renvoi true, sinon false
    public Boolean FindPierre(int px, int py) {
        Boolean retour = false;  
        // retour = true; // DEBUG  
        // for.... pour parcourir toutes les pierres
        for(Pierre onePierre:pierre) {
            // if une pierre correspond (x,y=px,py) retour = true;
            if(px == onePierre.getX() && py == onePierre.getY()) {
                retour = true;
            }
        }
        return retour;
    }

    // Nombre de libertés disponibles dans le groupe
    public Boolean Libertes(int tabMap[][], int tailleMap) {
        Boolean retour = false;
        
        // On parcours toutes les pierres du groupe :
        // Si au moins une pierre possède une liberté, on renvoi true
        // Sinon, on renvoi false        
        for(Pierre onePierre:pierre) {
            int voisinX = 0;
            int voisinY = 0;

            for(int i = 0; i < 4; i++) {

                if(i == 0) {                    // Haut
                    voisinX = onePierre.getX();
                    voisinY = onePierre.getY()-1;
                } else if(i == 1) {             // Droite
                    voisinX = onePierre.getX()+1;
                    voisinY = onePierre.getY();
                } else if(i == 2) {             // Bas
                    voisinX = onePierre.getX();
                    voisinY = onePierre.getY()+1;                        
                } else if(i == 3) {             // Gauche
                    voisinX = onePierre.getX()-1;
                    voisinY = onePierre.getY();
                }                

                // Si on est au bord, on ne fait rien (!)
                if(!((voisinX < 0 && i == 3) || (voisinY < 0 && i == 0) || (voisinX >= tailleMap && i == 1) || (voisinY >= tailleMap && i == 2))) {

                    // System.out.println("    i = "+i+" ; X = "+onePierre.getX()+" ; Y = "+onePierre.getY());
                    if(tabMap[voisinX][voisinY] == 0) {
                        retour = true;
                    }
                }
            }
        }
        return retour;
    }

    public int mortGroupe(int tabMap[][]) {
        int nbrPrisonnier = 0;

        for(Pierre onePierre:pierre) {
            tabMap[onePierre.getX()][onePierre.getY()] = 0;
            // pierre.remove(onePierre);
            nbrPrisonnier++;
        }

        return nbrPrisonnier;
    }

    // On affiche la liste des pierres du groupe
    public ArrayList<Pierre> ListePierre() {
        return pierre; 
    }
}