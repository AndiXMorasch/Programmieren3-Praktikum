package com.example.swingolf;

public class Textformatierung {
    private static final int MAXKAPAZITAET = 10;
    private static final int XSTARTPUNKT = 55;
    private static final int YSTARTPUNKT = 100;
    private static final int OFFSET = 40;
    private static final int TEXTSIZEKLEIN = 15;
    private static final int TEXTSIZEGROSS = 25;
    private static final int XSTARTPUNKTFELDER = 0;
    private static final int YSTARTPUNKTFELDER = 110;
    private static final int GRIDOFFSETFELDER = 250;

    public int getMaxkapazitaet() {
        return MAXKAPAZITAET;
    }

    public int getXstartpunkt() {
        return XSTARTPUNKT;
    }

    public int getYstartpunkt() {
        return YSTARTPUNKT;
    }

    public int getOffset() {
        return OFFSET;
    }

    public int getTextsizeKlein() {
        return TEXTSIZEKLEIN;
    }
    public int getTextsizeGross() {
        return TEXTSIZEGROSS;
    }

    public int getXstartpunktfelder() {
        return XSTARTPUNKTFELDER;
    }

    public int getYstartpunktfelder() {
        return YSTARTPUNKTFELDER;
    }

    public int getXgridoffsetfelder() {
        return GRIDOFFSETFELDER;
    }

    public boolean containsWhitespace(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }
}
