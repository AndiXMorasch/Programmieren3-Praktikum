package de.hsos.prog3.ab02.ui;

import de.hsos.prog3.ab02.util.EinUndAusgabe;

public class NutzerEingabeTest {
    public static void main(String[] args) {
        EinUndAusgabe einUndAusgabe = new EinUndAusgabe();
        NutzerEingabe nutzerEingabe = new NutzerEingabe(einUndAusgabe);
        nutzerEingabe.wahrscheinlichkeitDerBesiedlung();
        nutzerEingabe.anzahlDerSimulationsschritte();
        nutzerEingabe.anzahlZellenDesSpielfelds();
    }
}
