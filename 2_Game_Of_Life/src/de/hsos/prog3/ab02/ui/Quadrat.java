package de.hsos.prog3.ab02.ui;

import de.hsos.prog3.ab02.util.Interaktionsbrett;

public class Quadrat {
    private int x;
    private int y;
    private int seitenlaenge;

    public Quadrat(int x, int y, int seitenlaenge) {
        setX(x);
        setY(y);
        setSeitenlaenge(seitenlaenge);
    }

    private void setX(int x) {
        if(x < 0) {
            System.out.println("Der x Wert, darf nicht kleiner als 0 sein!");
            return;
        }
        this.x = x;
    }

    private void setY(int y) {
        if(y < 0) {
            System.out.println("Der y Wert, darf nicht kleiner als 0 sein!");
            return;
        }
        this.y = y;
    }

    private void setSeitenlaenge(int seitenlaenge) {
        if (seitenlaenge <= 0) {
            System.out.println("Das Quadrat darf keine Seitenlaenge kleiner gleich 0 haben!");
            return;
        }
        this.seitenlaenge = seitenlaenge;
    }

    public void darstellenRahmen(Interaktionsbrett ib) {
        ib.neuesRechteck(this.x, this.y, this.seitenlaenge, this.seitenlaenge);
    }

    public void darstellenFuellung(Interaktionsbrett ib) {
        darstellenRahmen(ib);
        for(int i = 0; i < this.seitenlaenge; i++) {
            ib.neueLinie(this.x + i, this.y, this.x + i, this.y + this.seitenlaenge);
        }
    }
}
