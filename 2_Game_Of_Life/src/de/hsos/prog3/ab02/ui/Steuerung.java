package de.hsos.prog3.ab02.ui;

import de.hsos.prog3.ab02.logik.BeiAenderung;
import de.hsos.prog3.ab02.logik.Simulator;
import de.hsos.prog3.ab02.util.EinUndAusgabe;
import de.hsos.prog3.ab02.util.Interaktionsbrett;

public class Steuerung implements BeiAenderung {
    public NutzerEingabe nutzerEingabe;
    public Simulator simulator;
    public SpielfeldDarstellung spielfeldDarstellung;

    public void initialisierung() throws InterruptedException {
        Interaktionsbrett ib = new Interaktionsbrett();
        EinUndAusgabe einUndAusgabe = new EinUndAusgabe();

        this.nutzerEingabe = new NutzerEingabe(einUndAusgabe);
        this.spielfeldDarstellung = new SpielfeldDarstellung(ib);

        this.simulator = new Simulator();
        this.simulator.anmeldenFuerAktualisierungBeiAenderung(this);
        startDesSpiels();
    }

    private void startDesSpiels() throws InterruptedException {
        int anzahlZellen = this.nutzerEingabe.anzahlZellenDesSpielfelds();
        int wahrscheinlichkeit = this.nutzerEingabe.wahrscheinlichkeitDerBesiedlung();
        this.simulator.berechneAnfangsGeneration(anzahlZellen, wahrscheinlichkeit);
        folgegenerationen();
    }

    private void folgegenerationen() throws InterruptedException {
        boolean weitereVeraenderung = true;
        while (weitereVeraenderung) {
            int anzahlSimulationsschritte = this.nutzerEingabe.anzahlDerSimulationsschritte();
            if (anzahlSimulationsschritte < 0) {
                System.out.println("Programm wird beendet, tschüüüüss...");
                // System exit ist nicht besonders clean... jedoch weiß ich nicht wie ich sonst das Interaktionsbrett schließen kann...
                System.exit(0);
            }
            this.simulator.berechneFolgeGeneration(anzahlSimulationsschritte);
            if (this.simulator.getKeineWeitereVeraenderung()) {
                weitereVeraenderung = false;
            }
        }
    }

    @Override
    public void aktualisiere(boolean[][] neu) {
        spielfeldDarstellung.abwischen();
        spielfeldDarstellung.spielfeldDarstellen(neu);
    }
}
