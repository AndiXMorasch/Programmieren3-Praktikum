package de.hsos.prog3.AndreasMorasch.ab01;

import de.hsos.prog3.AndreasMorasch.ab01.audio.adapter.Adapter;
import de.hsos.prog3.AndreasMorasch.ab01.orchester.DirigentIn;
import de.hsos.prog3.AndreasMorasch.ab01.orchester.Instrument;
import de.hsos.prog3.AndreasMorasch.ab01.orchester.MusikerIn;
import de.hsos.prog3.AndreasMorasch.ab01.orchester.Orchester;
import de.hsos.prog3.audio.SimpleAudioPlayer;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.net.URL;

public class App {

    public static void main(String[] args) throws IOException {
        Set<Nachbar> linkedHashSet = new LinkedHashSet<>();

        linkedHashSet.add(new Nachbar("Stefan", "Mueller"));
        linkedHashSet.add(new Nachbar("Hans", "Meier"));
        linkedHashSet.add(new Nachbar("Lena", "Klein"));
        linkedHashSet.add(new Nachbar("Lisa", "Gross"));

        System.out.print("Hallo ");

        int zaehler = 1;

        for (Nachbar i : linkedHashSet) {
            System.out.print(i.vorname + " " + i.nachname);
            if (zaehler == linkedHashSet.size()) {
                System.out.print(".");
                break;
            }
            System.out.print(", ");
            zaehler++;
        }

        // Zu Aufgabe 1.2:

        /*
        URL url = App.class.getResource("/Baritone.wav");
        SimpleAudioPlayer player = new SimpleAudioPlayer(url);
        player.setDebug(false);
        player.verboseLogging(true);
        player.play(0);*/

        // Zu Aufgabe 1.3:

        /*Adapter adapter = new Adapter();
        adapter.tonAn();
        adapter.einmaligAbspielen(url);
        adapter.wiederholtAbspielen(url, 5);
        adapter.tonAus();
        adapter.einmaligAbspielen(url);*/

        // Zu Aufgabe 1.4:

        String audioDatei = "/All_Together.wav";
        Orchester orchester = new Orchester("HSOS Titty Twister Orchestra", audioDatei);

        DirigentIn karajan = new DirigentIn("Karajan");
        orchester.setDirigentIn(karajan);

        MusikerIn trompete = new MusikerIn("Dirk die Lunge Mueller", Instrument.SAXOPHON);
        MusikerIn akkordion = new MusikerIn("Akki Taste", Instrument.AKKORDION);
        MusikerIn drum = new MusikerIn("Das Biest", Instrument.SCHLAGZEUG);
        orchester.addMusikerIn(trompete);
        orchester.addMusikerIn(akkordion);
        orchester.addMusikerIn(drum);

        orchester.proben();
        orchester.auftreten();
        orchester.spielen();
    }
}
