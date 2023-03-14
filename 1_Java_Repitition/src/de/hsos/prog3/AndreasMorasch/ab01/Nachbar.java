package de.hsos.prog3.AndreasMorasch.ab01;

import java.util.Objects;
import java.util.Set;

public class Nachbar {
    String vorname;
    String nachname;

    public Nachbar(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nachbar nachbar = (Nachbar) o;
        return Objects.equals(vorname, nachbar.vorname) && Objects.equals(nachname, nachbar.nachname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vorname, nachname);
    }
}
