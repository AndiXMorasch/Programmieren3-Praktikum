package de.hsos.prog3.ab02.ui;

import de.hsos.prog3.ab02.util.Interaktionsbrett;

public class SpielfeldDarstellungTest {
    public static void main(String[] args) {
        Interaktionsbrett ib = new Interaktionsbrett();
        SpielfeldDarstellung spielfeldDarstellung = new SpielfeldDarstellung(ib);
        boolean[][] bool = new boolean[10][10];

        for (int row = 0; row < bool.length; row++) {
            for (int col = 0; col < bool[row].length; col++) {
                if(col%2==0){
                    bool[row][col] = true;
                } else {
                    bool[row][col] = false;
                }
            }
        }
        spielfeldDarstellung.spielfeldDarstellen(bool);
    }
}
