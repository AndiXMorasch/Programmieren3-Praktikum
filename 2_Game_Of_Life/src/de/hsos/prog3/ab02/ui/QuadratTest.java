package de.hsos.prog3.ab02.ui;

import de.hsos.prog3.ab02.util.Interaktionsbrett;

public class QuadratTest {
    public static void main(String[] args) {
        Interaktionsbrett ib = new Interaktionsbrett();
        Quadrat quadrat = new Quadrat(0, 0, 30);
        //quadrat.darstellenRahmen(ib);
        quadrat.darstellenFuellung(ib);
    }
}
