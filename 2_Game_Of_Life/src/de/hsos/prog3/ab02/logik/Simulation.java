package de.hsos.prog3.ab02.logik;

public interface Simulation {
    void berechneAnfangsGeneration(int anzahlDerZellen, int wahrscheinlichkeitDerBesiedlung);
    void berechneFolgeGeneration(int berechnungsschritte) throws InterruptedException;
    void anmeldenFuerAktualisierungBeiAenderung(BeiAenderung beiAenderung);
}
