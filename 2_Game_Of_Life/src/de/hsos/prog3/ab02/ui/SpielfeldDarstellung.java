package de.hsos.prog3.ab02.ui;

import de.hsos.prog3.ab02.util.Interaktionsbrett;

public class SpielfeldDarstellung {
    private Interaktionsbrett ib;
    public static final int darstellungsbereich = 350;
    public static final int margin = 10;

    public SpielfeldDarstellung(Interaktionsbrett ib) {
        this.ib = ib;
    }

    public void spielfeldDarstellen(boolean[][] spielfeld) {

        int seitenlaenge = darstellungsbereich / spielfeld.length;
        int yWanderer = 0;
        int xWanderer = 0;

        for (int row = 0; row < spielfeld.length; row++) {
            for (int col = 0; col < spielfeld[row].length; col++) {
                Quadrat quadrat = new Quadrat(xWanderer + margin, yWanderer + margin, seitenlaenge);
                if(spielfeld[row][col]) {
                    quadrat.darstellenFuellung(this.ib);
                }
                quadrat.darstellenRahmen(this.ib);
                xWanderer+=seitenlaenge;
            }
            yWanderer+=seitenlaenge;
            xWanderer = 0;
        }
    }

    public void abwischen() {
        this.ib.abwischen();
    }
}
