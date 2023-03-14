package de.hsos.prog3.ab4.pong.ui;

import de.hsos.prog3.ab4.pong.util.Interaktionsbrett;

public class Rechteck {
    private int x;
    private int y;
    private int breite;
    private int hoehe;

    public Rechteck(int x, int y, int breite, int hoehe) {
        setX(x);
        setY(y);
        setBreite(breite);
        setHoehe(hoehe);
    }

    public void setX(int x) {
        if(x < 0) {
            System.out.println("Der x Wert, darf nicht kleiner als 0 sein!");
            return;
        }
        this.x = x;
    }

    public void setY(int y) {
        if(y < 0) {
            System.out.println("Der y Wert, darf nicht kleiner als 0 sein!");
            return;
        }
        this.y = y;
    }

    public void setBreite(int breite) {
        if(breite < 0) {
            System.out.println("Der Breite Wert, darf nicht kleiner als 0 sein!");
            return;
        }
        this.breite = breite;
    }

    public void setHoehe(int hoehe) {
        if(hoehe < 0) {
            System.out.println("Der Höhe Wert, darf nicht kleiner als 0 sein!");
            return;
        }
        this.hoehe = hoehe;
    }

    public void darstellenRahmen(Interaktionsbrett ib) {
        ib.neuesRechteck(this.x, this.y, this.breite, this.hoehe);
    }

    public void darstellenFuellung(Interaktionsbrett ib) {
        for(int i = 0; i < this.breite; i++) {
            ib.neueLinie(this.x + i, this.y, this.x + i, this.y + this.hoehe);
        }
    }

    public void darstellenMittlereLinie(Interaktionsbrett ib) {
        ib.neueLinie(x+mitteInX(), y, x+mitteInX(), y+this.hoehe);
    }

    public void verschiebe(int dx, int dy) {
        setX(this.x += dx);
        setY(this.y += dy);
    }

    public void verschiebeNach(int x, int y) {
        setX(x);
        setY(y);
    }

    public boolean ueberschneidet(Rechteck o) {
        if (this.oben() > o.unten() || this.unten() < o.oben()) {
            return false;
        }
        if (this.rechts() < o.links() || this.links() > o.rechts()) {
            return false;
        }
        return true;
    }

    public int oben() {
        return this.y;
    }

    public int unten() {
        return this.y + hoehe();
    }

    public int links() {
        return this.x;
    }

    public int rechts() {
        return this.x + breite();
    }

    public int breite() {
        return this.breite;
    }

    public int hoehe() {
        return this.hoehe;
    }

    public int mitteInY() {
        return (this.hoehe / 2) + 1;
    }

    public int mitteInX() {
        return (this.breite / 2 + 1);
    }

}
