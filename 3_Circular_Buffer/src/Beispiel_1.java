public class Beispiel_1 {
    public static void main(String[] args) {
        Ringpuffer<String> ringpuffer = new Ringpuffer<String>(5, true, true);

        // Beispiel: Videoaufnahmen, die nach 5 Tagen automatisch überschrieben werden

        ringpuffer.add("Vid_1");
        ringpuffer.add("Vid_2");
        ringpuffer.add("Vid_3");
        ringpuffer.add("Vid_4");
        ringpuffer.remove();
        ringpuffer.remove();
        ringpuffer.add("Vid_5");
        ringpuffer.add("Vid_6");
        ringpuffer.add("Vid_7");

        ringpuffer.ausgabeDesRingpuffers();

        ringpuffer.toStringWritePos();
        ringpuffer.toStringReadPos();

        System.out.println(ringpuffer.contains("Vid_6")); // true erwartet
        System.out.println(ringpuffer.contains("Vid_1")); // false erwartet
    }
}
