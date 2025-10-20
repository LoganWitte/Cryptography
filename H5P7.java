// Arup Guha, Logan Witte
// Originally written in Fall 2006, edited on 10/23/07 for CIS 3362.
// Modified by Logan Witte for H5 on 10/19/25 for CIS 3362.

import java.math.BigInteger;
import java.util.*;

public class H5P7 {

    // Tests MillarRabin algorithm vs Fermat algorithm.
    public static void main(String[] args) {

        // Testing parameters
        final int NUM_TRIALS = 10000000;
        final int NUM_A_VALUES = 50;
        final int SEED_START = 0;
        final int SEED_ITERATIONS = 1;

        Random r = new Random();

        for (int i = 0; i < SEED_ITERATIONS; i++) {

            long startTime = System.currentTimeMillis();

            r.setSeed(SEED_START + i);

            int[] fermatResults = new int[NUM_A_VALUES + 1];
            int[] millerRabinResults = new int[NUM_A_VALUES + 1];

            // Runs tests
            for (int j = 0; j < NUM_TRIALS; j++) {
                BigInteger n = generateValidComposite(NUM_A_VALUES, r);
                int fermatResult = FermatTest(n, NUM_A_VALUES, r);
                int millerRabinResult = MillerRabinTest(n, NUM_A_VALUES, r);
                fermatResults[fermatResult]++;
                millerRabinResults[millerRabinResult]++;
                // System.out.printf("Test #%d:\tn=%d,\tFermat=%d,\tMillerRabin=%d\n", i+1, n, fermatResult, millerRabinResult);
            }

            long endTime = System.currentTimeMillis();

            // Displays results
            if (i == 0) System.out.println("----------------------------------------");
            System.out.printf("Results (Tests: %d, Seed: %d, Took: %ss):\n", NUM_TRIALS, SEED_START + i, roundTime(endTime - startTime));
            System.out.println("Outcome\tFermat\tMiller-Rabin");
            for (int j = 0; j <= NUM_A_VALUES; j++) {
                if (fermatResults[j] == 0 && millerRabinResults[j] == 0)
                    continue;
                System.out.printf("%d:\t%d\t%d\n", j, fermatResults[j], millerRabinResults[j]);
            }
            System.out.println("----------------------------------------");

        }

    }

    // Rounds time in nanoseconds to milliseconds with 2 decimal places.
    public static String roundTime(long timeInMs) {
        return String.format("%.2f", timeInMs / 1000.0);
    }

    // Generates a random composite such that (n !| {2, 3, 5}) AND (10^8 < n < 10^9).
    public static BigInteger generateValidComposite(int numTests, Random r) {

        BigInteger min = BigInteger.TEN.pow(8);
        BigInteger max = BigInteger.TEN.pow(9);
        BigInteger range = max.subtract(min);
        BigInteger candidate;

        BigInteger ZERO = BigInteger.ZERO;
        BigInteger TWO = BigInteger.valueOf(2);
        BigInteger THREE = BigInteger.valueOf(3);
        BigInteger FIVE = BigInteger.valueOf(5);

        do {
            candidate = min.add(new BigInteger(range.bitLength(), r).mod(range));
        } while (candidate.isProbablePrime(numTests) ||
                candidate.mod(TWO).equals(ZERO) ||
                candidate.mod(THREE).equals(ZERO) ||
                candidate.mod(FIVE).equals(ZERO));

        return candidate;
    }

    // Runs one iteration of the Fermat Primality Test.
    private static boolean Fermat(BigInteger n, Random r) {

        // Ensures that temp > 1 and temp < n.
        BigInteger temp = BigInteger.ZERO;
        do {
            temp = new BigInteger(n.bitLength() - 1, r);
        } while (temp.compareTo(BigInteger.ONE) <= 0 || !temp.gcd(n).equals(BigInteger.ONE));

        // Just calculate temp^*(n-1) mod n
        BigInteger ans = temp.modPow(n.subtract(BigInteger.ONE), n);

        // Return true iff it passes the Fermat Test!
        return (ans.equals(BigInteger.ONE));
    }

    // Run Fermat() numTimes number of times.
    public static int FermatTest(BigInteger n, int numTimes, Random r) {

        for (int i = 0; i < numTimes; i++)
            if (!Fermat(n, r))
                return i;

        // If we get here, we assume n is prime.
        // This will be incorrect with a probability no greater than TODO
        return numTimes;
    }

    // Runs one iteration of the Miller-Rabin Primality Test.
    private static boolean MillerRabin(BigInteger n, Random r) {

        // Ensures that temp > 1 and temp < n.
        BigInteger temp = BigInteger.ZERO;
        do {
            temp = new BigInteger(n.bitLength() - 1, r);
        } while (temp.compareTo(BigInteger.ONE) <= 0);

        // Screen out n if our random number happens to share a factor with n.
        if (!n.gcd(temp).equals(BigInteger.ONE))
            return false;

        // For debugging, prints out the integer to test with.
        // System.out.println("Testing with " + temp);

        BigInteger base = n.subtract(BigInteger.ONE);
        BigInteger TWO = new BigInteger("2");

        // Figure out the largest power of two that divides evenly into n-1.
        int k = 0;
        while ((base.mod(TWO)).equals(BigInteger.ZERO)) {
            base = base.divide(TWO);
            k++;
        }

        // This is the odd value r, as described in our text.
        // System.out.println("base is " + base);

        BigInteger curValue = temp.modPow(base, n);

        // If this works out, we just say it's prime.
        if (curValue.equals(BigInteger.ONE))
            return true;

        // Otherwise, we will check to see if this value successively
        // squared ever yields -1.
        for (int i = 0; i < k; i++) {

            // We need to really check n-1 which is equivalent to -1.
            if (curValue.equals(n.subtract(BigInteger.ONE)))
                return true;

            // Square this previous number - here I am just doubling the
            // exponent. A more efficient implementation would store the
            // value of the exponentiation and square it mod n.
            else
                curValue = curValue.modPow(TWO, n);
        }

        // If none of our tests pass, we return false. The number is
        // definitively composite if we ever get here.
        return false;
    }

    // Run MillerRabin() numTimes number of times.
    public static int MillerRabinTest(BigInteger n, int numTimes, Random r) {

        for (int i = 0; i < numTimes; i++)
            if (!MillerRabin(n, r))
                return i;

        // If we get here, we assume n is prime.
        // This will be incorrect with a probability no greater than (1/4)^numTimes.
        return numTimes;
    }
}