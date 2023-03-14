package de.hsos.prog3.AndreasMorasch.ab01.audio.adapter;

import de.hsos.prog3.AndreasMorasch.ab01.audio.StdAudioPlayer;
import de.hsos.prog3.audio.SimpleAudioPlayer;

import java.io.IOException;
import java.net.URL;

public class Adapter implements StdAudioPlayer {

    private boolean istDebug = true;

    @Override
    public void einmaligAbspielen(URL url) throws IOException {
        SimpleAudioPlayer player = new SimpleAudioPlayer(url);
        player.setDebug(this.istDebug);
        player.verboseLogging(true);
        player.play(0);
    }

    @Override
    public void wiederholtAbspielen(URL url, int anzWiederholungen) throws IOException {
        SimpleAudioPlayer player = new SimpleAudioPlayer(url);
        player.setDebug(this.istDebug);
        player.verboseLogging(true);
        player.play(anzWiederholungen - 1);
    }

    @Override
    public void tonAus() {
        this.istDebug = true;
    }

    @Override
    public void tonAn() {
        this.istDebug = false;
    }
}
