import java.math.BigInteger;
import java.util.HashMap;

public class H6P4 {
    public static void main(String Args[]) {

        /*
        // Verifies working converters
        String s = "catdog";
        if(!bigIntegerToString(stringToBigInteger(s)).equals(s)) {
            System.out.println("Converters not working");
            return;
        };
        */

        BigInteger q = new BigInteger("1234567890133"); // Prime
        BigInteger a = new BigInteger("726288716355"); // PR of 'q'
        BigInteger y = new BigInteger("268931642640"); // Public exponent / pub. key
        BigInteger[] ciphertext = { new BigInteger("299335298747"), new BigInteger("1172223606113"),
                new BigInteger("889598930003"), new BigInteger("557439201934"),
                new BigInteger("493347324365"), new BigInteger("308563064395"),
                new BigInteger("89330316409"), new BigInteger("284568396280"),
                new BigInteger("1011600488984"), new BigInteger("284687286118"),
                new BigInteger("78301339268"), new BigInteger("445083507637"),
                new BigInteger("184420679506"), new BigInteger("945520081269"),
                new BigInteger("113325085374"), new BigInteger("330613800716"),
                new BigInteger("801254124539"), new BigInteger("2492123523"),
                new BigInteger("189525982163"), new BigInteger("429945730554"),
                new BigInteger("884843344510"), new BigInteger("169440640565"),
                new BigInteger("518889437424"), new BigInteger("634600793624"),
                new BigInteger("2248173165"), new BigInteger("323732777084"),
                new BigInteger("575687437463"), new BigInteger("568722795104"),
                new BigInteger("891109133954"), new BigInteger("753816562536"),
                new BigInteger("1124597795350"), new BigInteger("970569441767"),
                new BigInteger("608617739348"), new BigInteger("63349455224"),
                new BigInteger("771764120123"), new BigInteger("778305272817"),
                new BigInteger("181728415187"), new BigInteger("585124549217"),
                new BigInteger("492606809251"), new BigInteger("383617280481"),
                new BigInteger("458984069941"), new BigInteger("197980928842"),
                new BigInteger("1082309772979"), new BigInteger("255983715074"),
                new BigInteger("775217121216"), new BigInteger("504345290258"),
                new BigInteger("1114180621864"), new BigInteger("18900995437"),
                new BigInteger("139742778128"), new BigInteger("205760018212"),
                new BigInteger("1098177806924"), new BigInteger("464716156995"),
                new BigInteger("159086739222"), new BigInteger("63106975262"),
                new BigInteger("251212528964"), new BigInteger("496548036082"),
                new BigInteger("1207655912195"), new BigInteger("764037801281"),
                new BigInteger("544740425341"), new BigInteger("289356820759"),
                new BigInteger("1073938227507"), new BigInteger("329586495737"),
                new BigInteger("989432039687"), new BigInteger("1210124857313"),
                new BigInteger("1190570442012"), new BigInteger("1222786515110") };

        long startTime = System.nanoTime();
        BigInteger x = babyStepGiantStep(a, y, q);
        double elapsedTime = (System.nanoTime() - startTime) / 1e9;

        if (x == null) {
            System.out.println("Found no x");
            return;
        }

        System.out.printf("Found %s after %.2fs\n", x.toString(), elapsedTime);
        System.out.println("Verifying y=a^x mod q: " + y.equals(a.modPow(x, q)));

        for (int i = 0; i + 1 < ciphertext.length; i += 2) {
            BigInteger c1 = ciphertext[i];
            BigInteger c2 = ciphertext[i + 1];
            BigInteger k = c1.modPow(x, q);
            BigInteger kInv = k.modInverse(q);
            BigInteger M = c2.multiply(kInv).mod(q);
            System.out.println(bigIntegerToString(M));
        }
    }

    // Solves for x in equation 'a^x = y (mod q)'
    public static BigInteger babyStepGiantStep(BigInteger a, BigInteger y, BigInteger q) {

        BigInteger m = q.sqrt().add(BigInteger.ONE);

        // Baby step: Build table of a^j mod q for j = 0, 1, ..., m-1
        HashMap<BigInteger, BigInteger> table = new HashMap<>();
        BigInteger aj = BigInteger.ONE; // a^j

        System.out.println("Building baby-step table...");
        for (BigInteger j = BigInteger.ZERO; j.compareTo(m) < 0; j = j.add(BigInteger.ONE)) {
            table.put(aj, j);
            aj = aj.multiply(a).mod(q);
            /*
             * if (j.mod(new BigInteger("100000")).equals(BigInteger.ZERO) &&
             * !j.equals(BigInteger.ZERO)) {
             * System.out.printf("  Baby steps: %s / %s\n", j, m);
             * }
             */
        }

        // Giant step: Compute a^(-m) mod q
        BigInteger am = a.modPow(m, q);
        BigInteger amInv = am.modInverse(q);

        System.out.println("Performing giant steps...");
        BigInteger gamma = y; // y * (a^(-m))^i

        for (BigInteger i = BigInteger.ZERO; i.compareTo(m) < 0; i = i.add(BigInteger.ONE)) {
            // Check if gamma is in our baby-step table
            if (table.containsKey(gamma)) {
                BigInteger j = table.get(gamma);
                BigInteger x = i.multiply(m).add(j);
                return x;
            }
            /*
             * if (i.mod(new BigInteger("100000")).equals(BigInteger.ZERO) &&
             * !i.equals(BigInteger.ZERO)) {
             * System.out.printf("  Giant steps: %s / %s\n", i, m);
             * }
             */
            gamma = gamma.multiply(amInv).mod(q);
        }

        return null;
    }

    // Converts from a bigInt to its string counterpart
    public static String bigIntegerToString(BigInteger encoded) {
        // Base case
        if (encoded.equals(BigInteger.ZERO)) {
            return "a";
        }
        StringBuilder result = new StringBuilder();
        BigInteger base = new BigInteger("26");
        BigInteger current = encoded;
        while (current.compareTo(BigInteger.ZERO) > 0) {
            BigInteger remainder = current.mod(base);
            char letter = (char) ('a' + remainder.intValue());
            result.append(letter);
            current = current.divide(base);
        }
        return result.reverse().toString();
    }
    
    // For testing bigIntegerToString
    public static BigInteger stringToBigInteger(String str) {
        BigInteger result = BigInteger.ZERO;
        BigInteger base = new BigInteger("26");
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int value = c - 'a';
            result = result.multiply(base).add(BigInteger.valueOf(value));
        }
        return result;
    }
}