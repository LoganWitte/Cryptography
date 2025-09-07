import java.util.*;

public class Substitution {

    /*
    public static String encrypt(String plaintext, char[] key) {

        int len = plaintext.length();

        // Builds ciphertext
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            sb.append(key[plaintext.charAt(i) - 'a']);
        }

        return sb.toString();
    }
    */

    public static String decrypt(String ciphertext, char[] key) {

        int len = ciphertext.length();

        // Builds plaintext
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            sb.append(key[ciphertext.charAt(i) - 'a']);
        }

        return sb.toString();
    }

    // Fills empty indices of key using closest matching frequency
    public static void matchFrequencies(float[] frequencies, char[] key, boolean[] used) {
        float[] englishFreqencies = {.082f, .015f, .028f, .043f, .127f, .022f, .020f, .061f, .070f, .002f, .008f, .040f, .024f, .067f, .075f, .019f, .001f, .060f, .063f, .091f, .028f, .010f, .023f, .001f, .020f, .001f};
        float current;
        float bestMatch;
        int bestMatchIndex = 0;

        for(int i = 0; i < 26; i++) {
            // Skip already filled indices
            if(key[i] != '\u0000') {
                continue;
            }
            // Skip non-occurring letters
            current = frequencies[i];
            if(current == 0f) {
                key[i] = '*';
                continue;
            }
            bestMatch = Float.POSITIVE_INFINITY;
            for(int j = 0; j < 26; j++) {
                if(!used[j] && Math.abs(current - englishFreqencies[j]) < bestMatch) {
                    bestMatch = Math.abs(current - englishFreqencies[j]);
                    bestMatchIndex = j;
                }
            }
            key[i] = (char) (bestMatchIndex + 'a');
            used[bestMatchIndex] = true;
        }
    }

    public static void setKey(char cipher, char plain, char[] key, boolean[] used) {
        key[cipher - 'a'] = plain;
        used[plain - 'a'] = true;
    }

    public static void main(String Args[]) {
        
        char[] key = new char[26];
        boolean[] used = new boolean[26];

        // Manual key modifications (testing)
        setKey('m', 'a', key, used);
        setKey('b', 's', key, used);
        setKey('c', 'm', key, used);
        setKey('d', 'h', key, used);
        setKey('e', 'w', key, used);
        setKey('f', 'r', key, used);
        setKey('g', 'x', key, used);
        setKey('h', 'd', key, used);

        // Ciphertext input
        Scanner scnr = new Scanner(System.in);
        System.out.println("--- Substitution Cipher ---");
        System.out.println("Ciphertext: ");
        String ciphertext = scnr.next();
        scnr.close();

        // Builds alphabet list
        List<Character> alphabet = new ArrayList<>();
        for(int i = 0; i < 26; i++) {
            alphabet.add((char)(i + 'a'));
        }
        
        // Calculates letter frequencies
        float frequencies[] = new float[26];
        int sum = 0;
        int len = ciphertext.length();
        for(int i = 0; i < len; i++) {
            frequencies[alphabet.indexOf(ciphertext.charAt(i))] += 1;
            sum += 1;
        }
        for(int i = 0; i < 26; i++) {
            frequencies[i] /= (float) sum;
        }

        // Fills key with best guess        
        matchFrequencies(frequencies, key, used);

        // Displays letter frequencies of plaintext
        System.out.println("\nFrequencies: ");
        for(int i = 0; i < 26; i++) {
            System.out.print(String.format("%s: %f ", (char) (i+'a'), frequencies[i]));
            if(i == 6 || i == 13 || i == 20 || i == 25) {System.out.println();}
        }

        // Displays key
        System.out.println("\nKey: ");
        for(int i = 0; i < 26; i++) {
            System.out.print(String.format("%s->%s ", (char) (i+'a'), key[i]));
            if(i == 12 || i == 25) {System.out.println();}
        }
        
        // Plaintext output
        System.out.println("\nPlaintext: ");
        String plaintext = decrypt(ciphertext, key);
        System.out.println(plaintext);
        System.out.println();

        // Searches for and displays common digrams & trigrams
        List<String> commonDigrams = new ArrayList<>(Arrays.asList("th", "he", "in", "er", "an", "re", "nd", "at", "on", "nt", "ha", "es", "st", "en", "ed", "to", "it", "ou", "ea", "hi", "is", "or", "ti", "as", "te", "et"));
        List<String> commonTrigrams = new ArrayList<>(Arrays.asList("the", "and", "ing", "her", "hat", "his", "tha", "ere", "for", "ent", "ion", "ter", "was", "you", "ith", "ver", "all", "wit", "thi"));

        System.out.println("Common Digrams Found: ");
        for(String digram : commonDigrams) {
            if(plaintext.contains(digram)) {
                System.out.print(String.format("%s, ", digram));
            }
        }
        System.out.println();
        System.out.println("Common Trigrams Found: ");
        for(String trigram : commonTrigrams) {
            if(plaintext.contains(trigram)) {
                System.out.print(String.format("%s, ", trigram));
            }
        }
        System.out.println();
    }
}
