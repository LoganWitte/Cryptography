import java.util.*;

public class Substitution {

    public static String encrypt(String plaintext, char[] key) {

        int len = plaintext.length();

        // Builds ciphertext
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            sb.append(key[plaintext.charAt(i) - 'a']);
        }

        return sb.toString();
    }

    public static String decrypt(String ciphertext, char[] key) {

        int len = ciphertext.length();

        // Builds decryption key array
        char[] decryptionKey = new char[26];
        for(int i = 0; i < 26; i++) {
            decryptionKey[key[i] - 'a'] = (char) (i + 'a');
        }

        // Builds plaintext
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            sb.append(decryptionKey[ciphertext.charAt(i) - 'a']);
        }

        return sb.toString();
    }

    // Fills empty indices of key using closest matching frequency
    public static void matchFrequencies(float[] frequencies, char[] key) {
        float[] englishFreqencies = {.082f, .015f, .028f, .043f, .127f, .022f, .020f, .061f, .070f, .002f, .008f, .040f, .024f, .067f, .075f, .019f, .001f, .060f, .063f, .091f, .028f, .010f, .023f, .001f, .020f, .001f};
        boolean[] used = new boolean[26];
        float current;
        float bestMatch;
        int bestMatchIndex = 0;

        for(int i = 0; i < 26; i++) {
            if(key[i] != '\u0000') {continue;}
            current = frequencies[i];
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

    public static void main(String Args[]) {
        
        char[] key = new char[26];

        // Known keys
        //key['a' + 0] = 'b'

        // Ciphertext input
        Scanner scnr = new Scanner(System.in);
        System.out.println("\nCiphertext: ");
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
        matchFrequencies(frequencies, key);

        // Displays letter frequencies of plaintext
        for(int i = 0; i < 26; i++) {
            System.out.print(String.format("%s: %f ", (char) (i+'a'), frequencies[i]));
            if(i == 8 || i == 17 || i == 25) {System.out.println();}
        }

        // Displays key
        System.out.println("Key: ");
        for(int i = 0; i < 26; i++) {
            System.out.print(String.format("%s->%s ", (char) (i+'a'), key[i]));
            if(i == 12 || i == 25) {System.out.println();}
        }
        
        // Plaintext output
        System.out.println("Plaintext: ");
        System.out.println(decrypt(ciphertext, key));

    }
}
