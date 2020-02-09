import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.io.*;

public class Historique {

	int[][] tabMap;

	ArrayList<Groupe> groupe;

	int scoreJ1;
	int scoreJ2;

	int tourJoueur;

	int cooX;
	int cooY;

	long timeJ1;
	long timeJ2;

	public Historique(int pTabMap[][], int pScoreJ1, int pScoreJ2, ArrayList<Groupe> pGroupe, int pTourJoueur, int pCooX, int pCooY, long pTimeJ1, long pTimeJ2) {

		tabMap = new int[pTabMap.length][pTabMap.length];
		groupe = new ArrayList<Groupe>();

		// On affecte les nouvelles valeurs de tableau
		for(int i = 0; i < pTabMap.length; i++) {
			for(int j = 0; j < pTabMap.length; j++) {
				tabMap[i][j] = pTabMap[i][j];
			}
		}

		// On affecte les nouveaux groupes
		for(Groupe thisGroupe : pGroupe) {
			groupe.add(thisGroupe);
		}

		// On affecte les scores
		scoreJ1 = pScoreJ1;
		scoreJ2 = pScoreJ2;

		// On affecte les tours du joueurs
		tourJoueur = pTourJoueur;

		// On affecte les coordonnées de la dernière pierre posée
		cooX = pCooX;
		cooY = pCooY;

		timeJ1 = pTimeJ1;
		timeJ2 = pTimeJ2;
	}

	// ===== Accesseurs ===== //

	public void getHistorique(int pTabMap[][], ArrayList<Groupe> pGroupe) {
		for(int i = 0; i < pTabMap.length; i++) {
			for(int j = 0; j < pTabMap.length; j++) {
				pTabMap[i][j] = tabMap[i][j];
			}
		}

		pGroupe.clear();
		for(Groupe thisGroupe : groupe) {
			pGroupe.add(thisGroupe);
		}
	}

	// Tester si les 2 MAP sont identiques pour empêcher le KO
	public boolean mapIdentiques(int thisMap[][]) {
		boolean isIdentiques = true;

		// On parcours tout les tableaux. S'ils sont identiques, alors impossible ! Sinon, on joue
		for(int i = 0; i < thisMap.length; i++) {
			for(int j = 0; j < thisMap.length; j++) {
				if(thisMap[i][j] != tabMap[i][j])
					isIdentiques = false;
			}
		}

		return isIdentiques; 
	}
	
	public int getScoreJ1() {
		return scoreJ1;
	}

	public int getScoreJ2() {
		return scoreJ2;
	}

	public int getTourJoueur() {
		return tourJoueur;
	}

	public int getTourJoueurSuivant() {
		int retourJoueur = (tourJoueur+1)%2;
		return retourJoueur;
	}

	public int getCooX() {
		return cooX;
	}

	public int getCooY() {
		return cooY;
	}

	public long getTimeJ1() {
		return timeJ1;
	}

	public long getTimeJ2() {
		return timeJ2;
	}

}