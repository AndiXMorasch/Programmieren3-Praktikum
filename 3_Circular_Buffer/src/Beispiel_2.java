public class Beispiel_2 {
    public static void main(String[] args) {
        Ringpuffer<String> ringpuffer = new Ringpuffer<String>(2, false, false);

        // Beispiel: Chatverläufe welche nicht automatisch gelöscht werden, sondern für immer bestehen bleiben

        ringpuffer.add("P1: Hallo\n");
        ringpuffer.add("P2: Wie gehts\n");
        ringpuffer.add("P1: Gut und dir\n");
        ringpuffer.add("P2: Alles bestens\n");
        ringpuffer.remove();
        ringpuffer.add("P1: Cool\n");
        ringpuffer.add("P1: Ok tschüss\n");
        ringpuffer.add("P2: Bis dann\n");
        ringpuffer.remove();

        ringpuffer.ausgabeDesRingpuffers();

        ringpuffer.toStringReadPos();
        ringpuffer.toStringWritePos();
    }
}
