package de.hsos.prog3.ab4.pong.ui;

import de.hsos.prog3.ab4.pong.util.Interaktionsbrett;

public class Ball {
    private final Rechteck form;
    private int bewegungInXProFrame;
    private int bewegungInYProFrame;

    public Ball(Spielfeld spielfeld) {
        this.form = new Rechteck(spielfeld.getSpielflaeche().mitteInX() + spielfeld.getMarginX() - 2,
                spielfeld.getSpielflaeche().mitteInY() + spielfeld.getMarginY(), 5, 5);
        this.bewegungInXProFrame = 4;   // Hier kann man noch an den Werten für die Geschwindigkeit spielen
        this.bewegungInYProFrame = 2;   // Hier kann man noch an den Werten für die Geschwindigkeit spielen
    }

    public Rechteck getForm() {
        return form;
    }

    public void bewegen(int anzahlFrames) { // TODO: Was muss hier noch mit Anzahl frames passieren?
        form.verschiebe(this.bewegungInXProFrame, this.bewegungInYProFrame);
    }

    public void darstellen(Interaktionsbrett io) {
        form.darstellenRahmen(io);
        form.darstellenFuellung(io);
    }

    public void umkehrenDerBewegungInX() {
        int vorzeichenaenderung = -1;
        this.bewegungInXProFrame *= vorzeichenaenderung;
    }

    public void umkehrenDerBewegungInY() {
        int vorzeichenaenderung = -1;
        this.bewegungInYProFrame *= vorzeichenaenderung;
    }

    public void ballSchnellerMachen() {
        this.bewegungInXProFrame += 2;
    }

    public void ballLangsamerMachen() {
        if(this.bewegungInXProFrame > 2) {
            this.bewegungInXProFrame -= 2;
        }
    }
}
