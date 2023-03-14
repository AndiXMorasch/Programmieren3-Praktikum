package de.hsos.prog3.ab4.pong.logik;

import de.hsos.prog3.ab4.pong.ui.Ball;
import de.hsos.prog3.ab4.pong.ui.Spieler;
import de.hsos.prog3.ab4.pong.ui.Spielfeld;
import java.util.Random;

public class KollisionsDetektion {
    Spielfeld spielfeld;
    Spieler spielerLinks;
    Spieler spielerRechts;
    Random random = new Random();

    public KollisionsDetektion(Spielfeld spielfeld, Spieler spielerLinks, Spieler spielerRechts) {
        setSpielfeld(spielfeld);
        setSpielerLinks(spielerLinks);
        setSpielerRechts(spielerRechts);
    }

    private void setSpielfeld(Spielfeld spielfeld) {
        if(spielfeld != null) {
            this.spielfeld = spielfeld;
        }
    }

    private void setSpielerLinks(Spieler spielerLinks) {
        if(spielerLinks != null) {
            this.spielerLinks = spielerLinks;
        }
    }

    private void setSpielerRechts(Spieler spielerRechts) {
        if(spielerRechts != null) {
            this.spielerRechts = spielerRechts;
        }
    }

    public void checkBeruehrungBallSpielfeldgrenzen(Ball ball) {
        if(((ball.getForm().oben() < this.spielfeld.getSpielflaeche().oben() || ball.getForm().unten() > this.spielfeld.getSpielflaeche().unten()))) {
            ball.umkehrenDerBewegungInY();
        }
    }

    public void checkBeruehrungBallMitSchlaeger(Ball ball) {
        int zufallszahl = random.nextInt(2);
        if((ball.getForm().ueberschneidet(spielerLinks.getSchlaeger())) || ball.getForm().ueberschneidet(spielerRechts.getSchlaeger())) {
            if(zufallszahl == 0) {
                // Fall 1: entgegengesetzte Richtung
                System.out.println("Schläger erkannt - Ball fliegt in entgegengesetzte Richtung.");
                ball.umkehrenDerBewegungInY();
            } else {
                // Fall 2: Einfallswinkel- gleich Ausfallswinkel
                System.out.println("Schläger erkannt - Einfallswinkel- gleich Ausfallswinkel");
            }
            ball.umkehrenDerBewegungInX();
        }
    }

    public BallPosition checkAusserhalbDesSpielfeldes(Ball ball) {
        if(ball.getForm().links() <= this.spielfeld.getSpielflaeche().links()) {
            spielerLinks.erhoehePunkte();
            ballZuruecksetzen(ball);
            return BallPosition.DRAUSSEN_LINKS;
        } else if (ball.getForm().rechts() >= this.spielfeld.getSpielflaeche().rechts()) {
            spielerRechts.erhoehePunkte();
            ballZuruecksetzen(ball);
            return BallPosition.DRAUSSEN_RECHTS;
        }
        return BallPosition.DRINNEN;
    }

    private void ballZuruecksetzen(Ball ball) {
        ball.getForm().verschiebeNach(spielfeld.getSpielflaeche().mitteInX() + spielfeld.getMarginX() - 2, spielfeld.getSpielflaeche().mitteInY() + spielfeld.getMarginY());
        ball.umkehrenDerBewegungInX();
        ball.umkehrenDerBewegungInY();
    }

    public enum BallPosition {DRINNEN, DRAUSSEN_LINKS, DRAUSSEN_RECHTS}
}
