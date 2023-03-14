package de.hsos.prog3.AndreasMorasch.ab01.orchester;

public class MusikerIn extends Mitglied {
    Instrument instrument;

    public MusikerIn(String name, Instrument instrument) {
        super();
        this.instrument = instrument;
    }

    public Instrument getInstrument() {
        return this.instrument;
    }
}
