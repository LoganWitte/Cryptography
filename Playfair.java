import java.util.*;

public class Playfair {

    // Encrypt plaintext using Playfair cipher
    public static String encrypt(String plaintext, char[] key) {
        plaintext = preprocess(plaintext); // preprocess into digraphs
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += 2) {
            char char1 = plaintext.charAt(i);
            char char2 = plaintext.charAt(i + 1);

            int[] pos1 = findPosition(char1, key);
            int[] pos2 = findPosition(char2, key);

            if (pos1[0] == pos2[0]) {
                // same row
                sb.append(key[pos1[0] * 5 + (pos1[1] + 1) % 5]);
                sb.append(key[pos2[0] * 5 + (pos2[1] + 1) % 5]);
            } else if (pos1[1] == pos2[1]) {
                // same column
                sb.append(key[((pos1[0] + 1) % 5) * 5 + pos1[1]]);
                sb.append(key[((pos2[0] + 1) % 5) * 5 + pos2[1]]);
            } else {
                // rectangle
                sb.append(key[pos1[0] * 5 + pos2[1]]);
                sb.append(key[pos2[0] * 5 + pos1[1]]);
            }
        }
        return sb.toString();
    }

    // Decrypt ciphertext using Playfair cipher
    public static String decrypt(String ciphertext, char[] key) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 2) {
            char char1 = ciphertext.charAt(i);
            char char2 = ciphertext.charAt(i + 1);

            int[] pos1 = findPosition(char1, key);
            int[] pos2 = findPosition(char2, key);

            if (pos1[0] == pos2[0]) {
                // same row
                sb.append(key[pos1[0] * 5 + (pos1[1] + 4) % 5]);
                sb.append(key[pos2[0] * 5 + (pos2[1] + 4) % 5]);
            } else if (pos1[1] == pos2[1]) {
                // same column
                sb.append(key[((pos1[0] + 4) % 5) * 5 + pos1[1]]);
                sb.append(key[((pos2[0] + 4) % 5) * 5 + pos2[1]]);
            } else {
                // rectangle
                sb.append(key[pos1[0] * 5 + pos2[1]]);
                sb.append(key[pos2[0] * 5 + pos1[1]]);
            }
        }
        return sb.toString();
    }

    // Preprocess plaintext into digraphs
    private static String preprocess(String text) {
        text = text.toLowerCase().replaceAll("[^a-z]", "").replace("j", "i");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c1 = text.charAt(i);
            char c2 = (i + 1 < text.length()) ? text.charAt(i + 1) : 'x';

            if (c1 == c2) {
                sb.append(c1).append('x');
            } else {
                sb.append(c1).append(c2);
                i++; // skip next char
            }
        }

        if (sb.length() % 2 != 0) {
            sb.append('x'); // pad final if odd
        }
        return sb.toString();
    }

    // Find row/col of char in key matrix
    private static int[] findPosition(char ch, char[] key) {
        for (int i = 0; i < 25; i++) {
            if (key[i] == ch) {
                return new int[]{i / 5, i % 5};
            }
        }
        return null;
    }

    // Build key matrix from keyword
    private static char[] buildKey(String keyInput) {
        keyInput = keyInput.toLowerCase();
        boolean[] seen = new boolean[26];
        char[] key = new char[25];
        int index = 0;

        for (char c : keyInput.toCharArray()) {
            if (c == 'j') c = 'i';
            if (!seen[c - 'a']) {
                seen[c - 'a'] = true;
                key[index++] = c;
            }
        }
        for (char c = 'a'; c <= 'z'; c++) {
            if (c == 'j') continue;
            if (!seen[c - 'a']) {
                seen[c - 'a'] = true;
                key[index++] = c;
            }
        }
        return key;
    }

    public static void main(String[] args) {
        //Scanner scnr = new Scanner(System.in);
        //System.out.print("Input text: ");
        //String input = scnr.nextLine().toLowerCase();
        String input = "yrpxafhvqgzlnmvniuahexicxqfpntfwrecpdrgxfonuyrtrnvrznqdhrqhqmhrnshvargvnkfharykarygufainucdfrydxxnynvnhcynvgdhpsnvvkrdgecpuaesroeqtrohvgxufchqspunemaeiwvgaudehahdwevnuxdzvehqsppkgcpnsnryvqbvdqyqofodkaihyqcvrqiueqtrkewrpeicvgcupfcdergyilxuaevnitqbosoccykphvdsczohvnqsqbpcqcualuqcvgaokwcuvqihyqnqwdopvgcuwkoahdotdhpqavpsvopsnvdfdbznvqbupxehopnsoxcuvcodnziebdtrfo";
        //scnr.close();

        String keyInput = "topazes";
        char[] key = buildKey(keyInput);

        System.out.println("Key Matrix:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(key[i * 5 + j] + " ");
            }
            System.out.println();
        }

        String decrypted = decrypt(input, key);
        System.out.println("Decrypted:\n" + decrypted);
        System.out.println();
    }
}
