package de.hsos.prog3.ab02.logik;

import de.hsos.prog3.ab02.ui.Quadrat;

import java.util.Arrays;
import java.util.Random;

public class Simulator implements Simulation {

    private boolean[][] spielfeld;
    private int anzahlFelder;
    private BeiAenderung beiAenderung;
    private Random random = new Random();
    private boolean keineWeitereVeraenderung;

    @Override
    public void berechneAnfangsGeneration(int anzahlDerZellen, int wahrscheinlichkeitDerBesiedlung) {
        this.anzahlFelder = anzahlDerZellen;

        this.spielfeld = new boolean[anzahlDerZellen][anzahlDerZellen];

        for (int row = 0; row < spielfeld.length; row++) {
            for (int col = 0; col < spielfeld[row].length; col++) {
                int zufallswert = random.nextInt(100);
                //System.out.println(zufallswert);
                if (zufallswert < wahrscheinlichkeitDerBesiedlung) {
                    spielfeld[row][col] = true;
                } else {
                    spielfeld[row][col] = false;
                }
            }
        }
        aktualisiere(this.spielfeld);
    }

    @Override
    public void berechneFolgeGeneration(int berechnungsschritte) throws InterruptedException {

        if (berechnungsschritte < 0) {
            return;
        }

        Thread.sleep(150);

        // Tiefe Kopie
        boolean[][] tmpSpielfeld = new boolean[anzahlFelder][anzahlFelder];
        for (int i = 0; i < this.spielfeld.length; i++) {
            tmpSpielfeld[i] = Arrays.copyOf(this.spielfeld[i], this.spielfeld.length);
        }

        for (int row = 0; row < tmpSpielfeld.length; row++) {
            for (int col = 0; col < tmpSpielfeld[row].length; col++) {
                if (tmpSpielfeld[row][col]) {
                    if (row == 0 && col == 0) {
                        blockObenLinks(row, col, "bewohnt", tmpSpielfeld);
                    } else if (row == 0 && col == tmpSpielfeld[row].length - 1) {
                        blockObenRechts(row, col, "bewohnt", tmpSpielfeld);
                    } else if (row == tmpSpielfeld.length - 1 && col == 0) {
                        blockUntenLinks(row, col, "bewohnt", tmpSpielfeld);
                    } else if (row == tmpSpielfeld.length - 1 && col == tmpSpielfeld[row].length - 1) {
                        blockUntenRechts(row, col, "bewohnt", tmpSpielfeld);
                    } else if (row == 0 && (col != 0 && col != tmpSpielfeld[row].length - 1)) {
                        blockOben(row, col, "bewohnt", tmpSpielfeld);
                    } else if (row == tmpSpielfeld.length - 1 && (col != 0 && col != tmpSpielfeld[row].length - 1)) {
                        blockUnten(row, col, "bewohnt", tmpSpielfeld);
                    } else if ((row != 0 && row != tmpSpielfeld.length - 1) && col == 0) {
                        blockLinks(row, col, "bewohnt", tmpSpielfeld);
                    } else if ((row != 0 && row != tmpSpielfeld.length - 1) && col == tmpSpielfeld[row].length - 1) {
                        blockRechts(row, col, "bewohnt", tmpSpielfeld);
                    } else {
                        standardBlock(row, col, "bewohnt", tmpSpielfeld);
                    }
                } else {
                    if (row == 0 && col == 0) {
                        blockObenLinks(row, col, "unbewohnt", tmpSpielfeld);
                    } else if (row == 0 && col == tmpSpielfeld[row].length - 1) {
                        blockObenRechts(row, col, "unbewohnt", tmpSpielfeld);
                    } else if (row == tmpSpielfeld.length - 1 && col == 0) {
                        blockUntenLinks(row, col, "unbewohnt", tmpSpielfeld);
                    } else if (row == tmpSpielfeld.length - 1 && col == tmpSpielfeld[row].length - 1) {
                        blockUntenRechts(row, col, "unbewohnt", tmpSpielfeld);
                    } else if (row == 0 && (col != 0 && col != tmpSpielfeld[row].length - 1)) {
                        blockOben(row, col, "unbewohnt", tmpSpielfeld);
                    } else if (row == tmpSpielfeld.length - 1 && (col != 0 && col != tmpSpielfeld[row].length - 1)) {
                        blockUnten(row, col, "unbewohnt", tmpSpielfeld);
                    } else if ((row != 0 && row != tmpSpielfeld.length - 1) && col == 0) {
                        blockLinks(row, col, "unbewohnt", tmpSpielfeld);
                    } else if ((row != 0 && row != tmpSpielfeld.length - 1) && col == tmpSpielfeld[row].length - 1) {
                        blockRechts(row, col, "unbewohnt", tmpSpielfeld);
                    } else {
                        standardBlock(row, col, "unbewohnt", tmpSpielfeld);
                    }
                }
            }
        }

        // Prüfen auf Gleichheit zweier Generationen
        boolean ungleich = false;
        for (int row = 0; row < spielfeld.length && !ungleich; row++) {
            for (int col = 0; col < spielfeld[row].length && !ungleich; col++) {
                if (this.spielfeld[row][col] != tmpSpielfeld[row][col]) {
                    ungleich = true;
                }
            }
        }
        if (ungleich) {
            aktualisiere(this.spielfeld);
        } else {
            System.out.println("Die Generationen haben sich nicht verändert! " +
                    "Keine weitere Berechnung möglich.");
            keineWeitereVeraenderung = true;
            return;
        }

        if(berechnungsschritte != 1) {
            berechneFolgeGeneration(berechnungsschritte-1);
        }
    }

    public boolean getKeineWeitereVeraenderung() {
        return keineWeitereVeraenderung;
    }

    private void blockObenLinks(int row, int col, String status, boolean[][] tmpSpielfeld) {
        int anzNachbarn = nachbarschaftRechts(row, col, tmpSpielfeld) + nachbarschaftUntenRechts(row, col, tmpSpielfeld) +
                nachbarschaftUnten(row, col, tmpSpielfeld);
        entscheidungUeberBewohner(row, col, status, anzNachbarn);
    }

    private void blockObenRechts(int row, int col, String status, boolean[][] tmpSpielfeld) {
        int anzNachbarn = nachbarschaftLinks(row, col, tmpSpielfeld) + nachbarschaftUnten(row, col, tmpSpielfeld) +
                nachbarschaftUntenLinks(row, col, tmpSpielfeld);
        entscheidungUeberBewohner(row, col, status, anzNachbarn);
    }

    private void blockUntenLinks(int row, int col, String status, boolean[][] tmpSpielfeld) {
        int anzNachbarn = nachbarschaftOben(row, col, tmpSpielfeld) + nachbarschaftObenRechts(row, col, tmpSpielfeld) +
                nachbarschaftRechts(row, col, tmpSpielfeld);
        entscheidungUeberBewohner(row, col, status, anzNachbarn);
    }

    private void blockUntenRechts(int row, int col, String status, boolean[][] tmpSpielfeld) {
        int anzNachbarn = nachbarschaftLinks(row, col, tmpSpielfeld) + nachbarschaftObenLinks(row, col, tmpSpielfeld) +
                nachbarschaftOben(row, col, tmpSpielfeld);
        entscheidungUeberBewohner(row, col, status, anzNachbarn);
    }

    private void blockOben(int row, int col, String status, boolean[][] tmpSpielfeld) {
        int anzNachbarn = nachbarschaftLinks(row, col, tmpSpielfeld) + nachbarschaftUntenLinks(row, col, tmpSpielfeld) +
                nachbarschaftUnten(row, col, tmpSpielfeld) + nachbarschaftUntenRechts(row, col, tmpSpielfeld) +
                nachbarschaftRechts(row, col, tmpSpielfeld);
        entscheidungUeberBewohner(row, col, status, anzNachbarn);
    }

    private void blockUnten(int row, int col, String status, boolean[][] tmpSpielfeld) {
        int anzNachbarn = nachbarschaftLinks(row, col, tmpSpielfeld) + nachbarschaftObenLinks(row, col, tmpSpielfeld) +
                nachbarschaftOben(row, col, tmpSpielfeld) + nachbarschaftObenRechts(row, col, tmpSpielfeld) +
                nachbarschaftRechts(row, col, tmpSpielfeld);
        entscheidungUeberBewohner(row, col, status, anzNachbarn);
    }

    private void blockLinks(int row, int col, String status, boolean[][] tmpSpielfeld) {
        int anzNachbarn = nachbarschaftOben(row, col, tmpSpielfeld) + nachbarschaftObenRechts(row, col, tmpSpielfeld) +
                nachbarschaftRechts(row, col, tmpSpielfeld) + nachbarschaftUntenRechts(row, col, tmpSpielfeld) +
                nachbarschaftUnten(row, col, tmpSpielfeld);
        entscheidungUeberBewohner(row, col, status, anzNachbarn);
    }

    private void blockRechts(int row, int col, String status, boolean[][] tmpSpielfeld) {
        int anzNachbarn = nachbarschaftOben(row, col, tmpSpielfeld) + nachbarschaftObenLinks(row, col, tmpSpielfeld) +
                nachbarschaftLinks(row, col, tmpSpielfeld) + nachbarschaftUntenLinks(row, col, tmpSpielfeld) +
                nachbarschaftUnten(row, col, tmpSpielfeld);
        entscheidungUeberBewohner(row, col, status, anzNachbarn);
    }

    private void standardBlock(int row, int col, String status, boolean[][] tmpSpielfeld) {
        int anzNachbarn = nachbarschaftLinks(row, col, tmpSpielfeld) + nachbarschaftRechts(row, col, tmpSpielfeld) +
                nachbarschaftOben(row, col, tmpSpielfeld) + nachbarschaftUnten(row, col, tmpSpielfeld) +
                nachbarschaftObenLinks(row, col, tmpSpielfeld) + nachbarschaftObenRechts(row, col, tmpSpielfeld) +
                nachbarschaftUntenLinks(row, col, tmpSpielfeld) + nachbarschaftUntenRechts(row, col, tmpSpielfeld);
        entscheidungUeberBewohner(row, col, status, anzNachbarn);
    }

    private void entscheidungUeberBewohner(int row, int col, String status, int anzNachbarn) {
        if (status.equals("bewohnt") && (anzNachbarn == 3 || anzNachbarn == 2)) {
            return;
        } else {
            this.spielfeld[row][col] = false;
        }

        if (status.equals("unbewohnt") && anzNachbarn == 3) {
            this.spielfeld[row][col] = true;
        }
    }

    private int nachbarschaftLinks(int row, int col, boolean[][] tmpSpielfeld) {
        int zaehler = 0;
        if (tmpSpielfeld[row][col - 1]) {
            zaehler++;
        }
        return zaehler;
    }

    private int nachbarschaftRechts(int row, int col, boolean[][] tmpSpielfeld) {
        int zaehler = 0;
        if (tmpSpielfeld[row][col + 1]) {
            zaehler++;
        }
        return zaehler;
    }

    private int nachbarschaftOben(int row, int col, boolean[][] tmpSpielfeld) {
        int zaehler = 0;
        if (tmpSpielfeld[row - 1][col]) {
            zaehler++;
        }
        return zaehler;
    }

    private int nachbarschaftObenLinks(int row, int col, boolean[][] tmpSpielfeld) {
        int zaehler = 0;
        if (tmpSpielfeld[row - 1][col - 1]) {
            zaehler++;
        }
        return zaehler;
    }

    private int nachbarschaftObenRechts(int row, int col, boolean[][] tmpSpielfeld) {
        int zaehler = 0;
        if (tmpSpielfeld[row - 1][col + 1]) {
            zaehler++;
        }
        return zaehler;
    }

    private int nachbarschaftUnten(int row, int col, boolean[][] tmpSpielfeld) {
        int zaehler = 0;
        if (tmpSpielfeld[row + 1][col]) {
            zaehler++;
        }
        return zaehler;
    }

    private int nachbarschaftUntenLinks(int row, int col, boolean[][] tmpSpielfeld) {
        int zaehler = 0;
        if (tmpSpielfeld[row + 1][col - 1]) {
            zaehler++;
        }
        return zaehler;
    }

    private int nachbarschaftUntenRechts(int row, int col, boolean[][] tmpSpielfeld) {
        int zaehler = 0;
        if (tmpSpielfeld[row + 1][col + 1]) {
            zaehler++;
        }
        return zaehler;
    }

    @Override
    public void anmeldenFuerAktualisierungBeiAenderung(BeiAenderung beiAenderung) {
        this.beiAenderung = beiAenderung;
    }

    private void aktualisiere(boolean[][] neu) {
        if (beiAenderung != null) {
            this.beiAenderung.aktualisiere(neu);
        }
    }
}
