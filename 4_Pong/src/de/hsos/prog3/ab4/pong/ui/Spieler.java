package de.hsos.prog3.ab4.pong.ui;

import de.hsos.prog3.ab4.pong.util.Interaktionsbrett;

public class Spieler {
    private final Spielfeld spielfeld;
    private final Rechteck schlaeger;
    private int punktestand;
    private int x;
    private int y;

    public Spieler(Spielfeld spielfeld, int x, int y) {
        this.spielfeld = spielfeld;
        this.punktestand = 0;
        this.setX(x);
        this.setY(y);
        this.schlaeger = new Rechteck(this.x, this.y, spielfeld.getSpielfeldBreite()/100, spielfeld.getSpielfeldHoehe()/10);
    }

    private void setX(int x) {
        this.x = x;
    }

    private void setY(int y) {
        this.y = y;
    }

    public Rechteck getSchlaeger() {
        return this.schlaeger;
    }

    public void darstellen(Interaktionsbrett io) {
        schlaeger.darstellenRahmen(io);
        schlaeger.darstellenFuellung(io);
    }

    public void aufwaerts() {
        if(this.schlaeger.ueberschneidet(spielfeld.getSpielflaeche()) && (schlaeger.oben() != spielfeld.getSpielflaeche().oben())) {
            schlaeger.verschiebe(0, -10);
        }
    }

    public void abwaerts() {
        if(this.schlaeger.ueberschneidet(spielfeld.getSpielflaeche()) && (schlaeger.unten() != spielfeld.getSpielflaeche().unten())) {
            schlaeger.verschiebe(0, 10);
        }
    }

    public void erhoehePunkte() {
        this.punktestand++;
        if(this.punktestand == 15) {
            System.exit(1);
        }
    }

    public void setzePunkteZurueck() {
        this.punktestand = 0;
    }

    public void spielstandDarstellen(Interaktionsbrett io, String spieler) {
        if(spieler.equals("links")) {
            io.neuerText(((spielfeld.getSpielfeldBreite() / 2) - 2) + spielfeld.getMarginX() + 20, 50, String.valueOf(this.punktestand));
        } else {
            io.neuerText(((spielfeld.getSpielfeldBreite() / 2) - 2) + spielfeld.getMarginX() - 20, 50, String.valueOf(this.punktestand));
        }
    }
}
