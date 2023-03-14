
public class main {
    public static void main(String[] args) {
        Ringpuffer<Integer> ringpuffer = new Ringpuffer<Integer>(4, true, true);

        int k = 0;
        for(int i = 0; i < 4; i++) {
            k += 1;
            ringpuffer.add(k);
        }

        ringpuffer.ausgabeDesRingpuffers();
        ringpuffer.add(++k);
        ringpuffer.ausgabeDesRingpuffers();
    }
}
