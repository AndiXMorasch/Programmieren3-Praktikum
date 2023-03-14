package de.hsos.prog3.ab4.pong.ui;

import de.hsos.prog3.ab4.pong.logik.KollisionsDetektion;
import de.hsos.prog3.ab4.pong.util.Interaktionsbrett;

public class PongSpiel implements Runnable {
    private Spielfeld spielfeld;
    private Spieler spielerLinks;
    private Spieler spielerRechts;
    private Ball ball;
    private KollisionsDetektion detektor;
    private boolean nochKeinThreadErzeugt;
    private final Interaktionsbrett io;
    private final int MSPF = 17;

    public PongSpiel() {
        this.io = new Interaktionsbrett();
        nochKeinThreadErzeugt = true;
        startAufstellung();
        willTastenInfo();
    }

    private void willTastenInfo() {
        this.io.willTasteninfo(this);
    }

    private void startAufstellung() {
        this.spielfeld = new Spielfeld();
        this.spielerLinks = new Spieler(spielfeld, 30, spielfeld.getSpielflaeche().mitteInY());
        this.spielerRechts = new Spieler(spielfeld, 750, spielfeld.getSpielflaeche().mitteInY());
        this.ball = new Ball(spielfeld);
        this.detektor = new KollisionsDetektion(spielfeld, spielerLinks, spielerRechts);
    }
    public void tasteGedrueckt(String s) throws InterruptedException {
        System.out.println(s);
        if(s.equals("a")) {
            this.spielerLinks.aufwaerts();
        } else if(s.equals("y")) {
            this.spielerLinks.abwaerts();
        } else if(s.equals("Oben")) {
            this.spielerRechts.aufwaerts();
        } else if(s.equals("Unten")) {
            this.spielerRechts.abwaerts();
        } else if(s.equals("s") && nochKeinThreadErzeugt) {
            nochKeinThreadErzeugt = false;
            new Thread(this).start();
        } else if (s.equals("e")) {
            System.exit(1);
        } else if (s.equals("f")) {
            this.ball.ballSchnellerMachen();
        } else if (s.equals("l")) {
            this.ball.ballLangsamerMachen();
        }
    }

    @Override
    public void run() {
        while(true) {
            long begonnen = System.currentTimeMillis();
            this.io.abwischen();
            this.spielfeld.darstellen(this.io);
            this.spielerLinks.darstellen(this.io);
            this.spielerRechts.darstellen(this.io);
            this.spielerLinks.spielstandDarstellen(this.io, "links");
            this.spielerRechts.spielstandDarstellen(this.io, "rechts");
            this.ball.darstellen(this.io);
            long aufgehoert = System.currentTimeMillis();
            this.ball.bewegen((int) ((aufgehoert-begonnen)/MSPF));
            this.detektor.checkBeruehrungBallMitSchlaeger(this.ball);
            this.detektor.checkBeruehrungBallSpielfeldgrenzen(this.ball);
            this.detektor.checkAusserhalbDesSpielfeldes(this.ball);
            if ((aufgehoert - begonnen) < MSPF) {
                try {
                    Thread.sleep(MSPF - (aufgehoert - begonnen));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
