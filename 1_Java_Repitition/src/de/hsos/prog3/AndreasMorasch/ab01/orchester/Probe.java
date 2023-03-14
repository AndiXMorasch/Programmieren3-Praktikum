package de.hsos.prog3.AndreasMorasch.ab01.orchester;

import de.hsos.prog3.AndreasMorasch.ab01.App;
import de.hsos.prog3.AndreasMorasch.ab01.audio.StdAudioPlayer;
import de.hsos.prog3.AndreasMorasch.ab01.audio.adapter.Adapter;

import java.io.IOException;
import java.net.URL;

/*public class Probe implements Verhalten {
    @Override
    public void spielen(Orchester orchester) {

        StdAudioPlayer audioPlayer = new Adapter();

        for (MusikerIn m : orchester.getMusikerInnen()) {
            URL url = App.class.getResource(m.getInstrument().getAudio());
            try {
                audioPlayer.tonAn();
                audioPlayer.einmaligAbspielen(url);
            } catch (IOException e) {
                System.out.println("Probe wird abgebrochen");
            }
        }
    }
}*/
