package de.hsos.prog3.ab02.ui;

import de.hsos.prog3.ab02.util.EinUndAusgabe;

public class NutzerEingabe {
    private EinUndAusgabe io;

    public NutzerEingabe(EinUndAusgabe io) {
        this.io = io;
    }

    int anzahlZellenDesSpielfelds() {
        int minAnzahl = 3;
        System.out.println("Bitte geben Sie die Anzahl der Zellen ein. Die Mindestanzahl beträgt " + minAnzahl + ".");
        int nutzerEingabeZellen = io.leseInteger();
        if(nutzerEingabeZellen >= minAnzahl){
            return nutzerEingabeZellen;
        }
        System.out.println("Ihre Eingabe " + nutzerEingabeZellen + " ist kleiner als " + minAnzahl + ".");
        return anzahlZellenDesSpielfelds();
    }

    int wahrscheinlichkeitDerBesiedlung() {
        System.out.println("Bitte geben Sie eine Wahrscheinlichkeit zwischen 1 und 100 ein.");
        int wahrscheinlichkeitDerBesiedlung = io.leseInteger();
        if (wahrscheinlichkeitDerBesiedlung <= 100 && wahrscheinlichkeitDerBesiedlung > 0) {
            return wahrscheinlichkeitDerBesiedlung;
        }
        System.out.println("Ihre Wahrscheinlichkeit " + wahrscheinlichkeitDerBesiedlung + " liegt nicht zwischen 1 und 100.");
        return wahrscheinlichkeitDerBesiedlung();
    }

    int anzahlDerSimulationsschritte() {
        int maxAnzahl = 100;
        System.out.println("Bitte geben Sie die Anzahl der Simulationsschritte ein. " +
                "Die Höchstanzahl beträgt " + maxAnzahl + ". (Abbruch mit negativer Zahl)");
        int nutzerEingabe = io.leseInteger();
        if (nutzerEingabe <= maxAnzahl && nutzerEingabe > 0) {
            return nutzerEingabe;
        } else if (nutzerEingabe < 0) {
            return -1;
        }
        return anzahlDerSimulationsschritte();
    }
}
