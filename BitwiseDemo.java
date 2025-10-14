import java.util.Random;

public class BitwiseDemo {

    public static void main(String[] args) {
        Random rand = new Random();

        // ---- Test 1: Random block ----
        System.out.println("Test 1: Random block");
        long block = setBitsRnd(rand);
        System.out.println("Block:");
        printBits(block);
        long hash = getXORHash(block);
        System.out.println("Hash:");
        printBits(hash);
        System.out.println();

        // ---- Test 2: All 1s ----
        System.out.println("Test 2: All 1s (0xFFFFFFFFFFFFFFFF)");
        block = 0xFFFFFFFFFFFFFFFFL;
        printBits(block);
        hash = getXORHash(block);
        printBits(hash);
        System.out.println();

        // ---- Test 3: All 0s ----
        System.out.println("Test 3: All 0s (0x0000000000000000)");
        block = 0x0000000000000000L;
        printBits(block);
        hash = getXORHash(block);
        printBits(hash);
        System.out.println();

        // ---- Test 4: Pattern ----
        System.out.println("Test 4: Pattern (0x0123456789ABCDEF)");
        block = 0x0123456789ABCDEFL;
        printBits(block);
        hash = getXORHash(block);
        printBits(hash);
        System.out.println();
    }

    // Just so we can see the bits.
    public static void printBits(long x) {
        for (int i = 63; i >= 0; i--) {
            if ((x & (1L << i)) != 0) System.out.print("1");
            else System.out.print("0");

            if (i % 8 == 0) System.out.println();
        }
    }

    // Returns a long with random bits.
    public static long setBitsRnd(Random rand) {
        long x = 0;
        for (int i = 0; i < 64; i++) {
            if (rand.nextInt(2) == 1) {
                x |= (1L << i);
            }
        }
        return x;
    }

    // Returns the XOR of each byte in block.
    public static long getXORHash(long block) {
        long mask = (1L << 8) - 1;
        long hash = 0;

        for (int i = 0; i < 8; i++) {
            hash ^= (block & mask);
            block >>>= 8;
        }

        return hash;
    }
}
