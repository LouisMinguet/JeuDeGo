import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Pierre {
    int x,y;

    // Définition d'une pierre (Coordonnées en X et Y)
    public Pierre(int px, int py) {
    	x = px;
    	y = py;
    }
    
    // Position X de la pierre
    int getX() {
        return x;
    }

    // Position Y de la pierre
    int getY() {
        return y;
    }
}