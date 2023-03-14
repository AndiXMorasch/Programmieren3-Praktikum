package de.hsos.prog3.AndreasMorasch.ab01.orchester;

import de.hsos.prog3.AndreasMorasch.ab01.App;
import de.hsos.prog3.AndreasMorasch.ab01.audio.StdAudioPlayer;
import de.hsos.prog3.AndreasMorasch.ab01.audio.adapter.Adapter;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashSet;

public class Orchester {
    String bezeichnung;
    DirigentIn dirigentIn;
    Collection<MusikerIn> musikerInnen = new LinkedHashSet<>();
    Verhalten verhalten;
    String audioDateiKonzert;

    public Orchester(String bezeichnung, String audioDateiKonzert) {
        this.bezeichnung = bezeichnung;
        this.audioDateiKonzert = audioDateiKonzert;
    }

    public void setDirigentIn(DirigentIn dirigent) {
        this.dirigentIn = dirigent;
    }

    public void addMusikerIn(MusikerIn musikerIn) {
        this.musikerInnen.add(musikerIn);
    }

    public Collection<MusikerIn> getMusikerInnen() {
        return this.musikerInnen;
    }

    public URL getAudiodateikonzert() {
        return App.class.getResource(audioDateiKonzert);
    }

    public void proben() {
        this.verhalten = new Probe();
    }

    public void auftreten() {
        this.verhalten = new Konzert();
    }

    public void spielen() {
        try {
            this.verhalten.spielen(this);
        } catch (IOException e) {
            System.out.println("Auftritt wird abgebrochen!");
        }
    }

    // Zu Aufgabe 1.5:

    private class Konzert implements Verhalten {

        @Override
        public void spielen(Orchester orchester) {
            StdAudioPlayer audioPlayer = new Adapter();
            URL url = orchester.getAudiodateikonzert();
            try {
                audioPlayer.tonAn();
                audioPlayer.einmaligAbspielen(url);
            } catch (IOException e) {
                System.out.println("Auftritt wird abgebrochen!");
            }
        }
    }

    private class Probe implements Verhalten {
        @Override
        public void spielen(Orchester orchester) {
            StdAudioPlayer audioPlayer = new Adapter();

            for (MusikerIn m : orchester.getMusikerInnen()) {
                URL url = App.class.getResource(m.getInstrument().getAudio());
                try {
                    audioPlayer.tonAn();
                    audioPlayer.einmaligAbspielen(url);
                } catch (IOException e) {
                    System.out.println("Probe wird abgebrochen!");
                }
            }
        }
    }
}
