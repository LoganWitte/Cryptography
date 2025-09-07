import java.util.*;

public class Substitution {

    /*
     * public static String encrypt(String plaintext, char[] key) {
     * 
     * int len = plaintext.length();
     * 
     * // Builds ciphertext
     * StringBuilder sb = new StringBuilder(len);
     * for(int i = 0; i < len; i++) {
     * sb.append(key[plaintext.charAt(i) - 'a']);
     * }
     * 
     * return sb.toString();
     * }
     */

    public static String decrypt(String ciphertext, char[] key) {

        int len = ciphertext.length();

        // Builds plaintext
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = ciphertext.charAt(i);
            sb.append(key[c - 'a']);
        }

        return sb.toString();
    }

    // Fills empty indices of key using closest matching frequency
    public static void matchFrequencies(float[] frequencies, char[] key, boolean[] used) {

        float[] englishFrequencies = { .082f, .015f, .028f, .043f, .127f, .022f, .020f, .061f, .070f, .002f, .008f,
                .040f, .024f, .067f, .075f, .019f, .001f, .060f, .063f, .091f, .028f, .010f, .023f, .001f, .020f,
                .001f };

        List<FrequencyPair> cipherFreqs = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            if (frequencies[i] > 0) {
                cipherFreqs.add(new FrequencyPair(frequencies[i], i));
            }
        }
        Collections.sort(cipherFreqs, Collections.reverseOrder());

        List<FrequencyPair> englishFreqs = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            englishFreqs.add(new FrequencyPair(englishFrequencies[i], i));
        }
        Collections.sort(englishFreqs, Collections.reverseOrder());

        // Match highest frequency cipher letters to highest frequency English letters
        for (int i = 0; i < cipherFreqs.size() && i < englishFreqs.size(); i++) {
            int cipherIndex = cipherFreqs.get(i).index;
            int plainIndex = englishFreqs.get(i).index;

            if (key[cipherIndex] == '\u0000' && !used[plainIndex]) {
                key[cipherIndex] = (char) (plainIndex + 'a');
                used[plainIndex] = true;
            }
        }

        // Fill remaining empty positions with unused letters
        for (int i = 0; i < 26; i++) {
            if (key[i] == '\u0000') {
                for (int j = 0; j < 26; j++) {
                    if (!used[j]) {
                        key[i] = (char) (j + 'a');
                        used[j] = true;
                        break;
                    }
                }
                if (key[i] == '\u0000') {
                    key[i] = '*';
                }
            }
        }
    }

    // Helper class for frequency matching
    static class FrequencyPair implements Comparable<FrequencyPair> {
        float frequency;
        int index;

        FrequencyPair(float frequency, int index) {
            this.frequency = frequency;
            this.index = index;
        }

        @Override
        public int compareTo(FrequencyPair other) {
            return Float.compare(this.frequency, other.frequency);
        }
    }

    public static void setKey(char cipher, char plain, char[] key, boolean[] used) {
        // Remove old mapping if it exists
        if (key[cipher - 'a'] != '\u0000') {
            used[key[cipher - 'a'] - 'a'] = false;
        }
        key[cipher - 'a'] = plain;
        used[plain - 'a'] = true;
    }

    public static void main(String Args[]) {

        char[] key = new char[26];
        boolean[] used = new boolean[26];

        // Manual key modifications (testing)
        // #1
        /*
         * setKey('m', 'a', key, used);
         * setKey('t', 'c', key, used);
         * setKey('d', 'h', key, used);
         * setKey('s', 'f', key, used);
         * setKey('b', 's', key, used);
         * setKey('l', 'u', key, used);
         * setKey('f', 'r', key, used);
         * setKey('q', 'i', key, used);
         * setKey('a', 'n', key, used);
         * setKey('c', 'm', key, used);
         * setKey('e', 'w', key, used);
         * setKey('p', 'p', key, used);
         * setKey('r', 'z', key, used);
         * setKey('y', 'b', key, used);
         * setKey('w', 'k', key, used);
         * setKey('v', 'g', key, used);
         * setKey('g', 'x', key, used);
         */

        // #2
        /*
        setKey('g', 'h', key, used);
        setKey('a', 'i', key, used);
        setKey('b', 'n', key, used);
        setKey('d','d', key, used);
        setKey('c', 'o', key, used);
        setKey('f', 'f', key, used);
        setKey('w', 's', key, used);
        setKey('x', 'm', key, used);
        setKey('l', 'a', key, used);
        setKey('r', 'w', key, used);
        setKey('n', 'l', key, used);
        setKey('v',  'p', key, used);
        setKey('k', 'c', key, used);
        setKey('e', 'u', key, used);
        setKey('z', 'k', key, used);
        setKey('u', 'x', key, used);
        setKey('i', 'v', key, used);
        */

        // Ciphertext input
        Scanner scnr = new Scanner(System.in);
        System.out.println("--- Substitution Cipher ---");
        System.out.println("Ciphertext: ");
        String ciphertext = scnr.nextLine().toLowerCase();
        scnr.close();

        // Calculates letter frequencies
        float frequencies[] = new float[26];
        int len = ciphertext.length();
        for (int i = 0; i < len; i++) {
            frequencies[ciphertext.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            frequencies[i] /= (float) len;
        }

        // Fills key with best guess
        matchFrequencies(frequencies, key, used);

        // Displays letter frequencies of ciphertext
        System.out.println("\nFrequencies: ");
        for (int i = 0; i < 26; i++) {
            System.out.print(String.format("%s: %.4f ", (char) (i + 'a'), frequencies[i]));
            if (i == 6 || i == 13 || i == 20 || i == 25) {
                System.out.println();
            }
        }

        // Displays key
        System.out.println("\nKey: ");
        for (int i = 0; i < 26; i++) {
            System.out.print(String.format("%s->%s ", (char) (i + 'a'), key[i]));
            if (i == 12 || i == 25) {
                System.out.println();
            }
        }

        // Plaintext output
        System.out.println("\nPlaintext: ");
        String plaintext = decrypt(ciphertext, key);
        System.out.println(plaintext);
        System.out.println();

        // Searches for and displays common digrams & trigrams
        List<String> commonDigrams = new ArrayList<>(Arrays.asList("th", "he", "in", "er", "an", "re", "nd", "at", "on",
                "nt", "ha", "es", "st", "en", "ed", "to", "it", "ou", "ea", "hi", "is", "or", "ti", "as", "te", "et"));
        List<String> commonTrigrams = new ArrayList<>(Arrays.asList("the", "and", "ing", "her", "hat", "his", "tha",
                "ere", "for", "ent", "ion", "ter", "was", "you", "ith", "ver", "all", "wit", "thi"));

        System.out.println("Common Digrams Found: ");
        for (String digram : commonDigrams) {
            if (plaintext.contains(digram)) {
                System.out.print(String.format("%s, ", digram));
            }
        }
        System.out.println();
        System.out.println("Common Trigrams Found: ");
        for (String trigram : commonTrigrams) {
            if (plaintext.contains(trigram)) {
                System.out.print(String.format("%s, ", trigram));
            }
        }
        System.out.println();
    }
}