import java.util.*;

public class AffineDecrypt {

    public static String affineDecrypt(String str, int a_inv, int b) {

        int len = str.length();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < len; i++) {
            sb.append((char) (((a_inv * ((str.charAt(i) - 'a' - b + 26)) % 26) + 'a'))); 
        }

        return sb.toString();
    }

    public static void main(String args[]) {

        final int[] a_inv_values = {1,3,5,7,9,11,15,17,19,21,23,25};

        Scanner scnr = new Scanner(System.in);

        System.out.print("Input: ");
        String input = scnr.next();

        scnr.close();

        String[][] decryptedStrings = new String[12][26];

        int a_index = 0;
        for(int a_inv : a_inv_values) {
            for(int i = 0; i < 26; i++) {
                decryptedStrings[a_index][i] = affineDecrypt(input, a_inv, i);
            }
            a_index ++;
        }

        String[] targetWords = {"the", "logan"};

        for(int i = 0; i < a_index; i++) {
            for(int j = 0; j < 26; j++) {
                for(String word : targetWords) {
                    if(decryptedStrings[i][j].contains(word)) {
                        System.out.println();
                        System.out.println(decryptedStrings[i][j]);
                    }
                }
            }
        }

    }
}