package de.hsos.prog3.ab4.pong.ui;

import de.hsos.prog3.ab4.pong.util.Interaktionsbrett;

public class Spielfeld {
    private final int marginX = 20;
    private final int marginY = 20;
    private final int spielfeldHoehe = 599;
    private final int spielfeldBreite = 749;
    private Rechteck spielfläche;

    public Spielfeld() {
        this.spielfläche = new Rechteck (this.marginX, this.marginY, this.spielfeldBreite, this.spielfeldHoehe);
    }

    public Rechteck getSpielflaeche() {
        return this.spielfläche;
    }

    public void darstellen(Interaktionsbrett io) {
        this.spielfläche.darstellenRahmen(io);
        this.spielfläche.darstellenMittlereLinie(io);
    }

    public int getSpielfeldHoehe() {
        return spielfeldHoehe;
    }

    public int getSpielfeldBreite() {
        return spielfeldBreite;
    }

    public int getMarginX() {
        return this.marginX;
    }

    public int getMarginY() {
        return this.marginY;
    }
}
