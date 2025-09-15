import java.util.*;

public class Railfence {

    // Encrypts plaintext using Rail Fence cipher with given number of rows and starting row
    public static String encrypt(String plaintext, int numRows, int startingRow) {

        int len = plaintext.length();
        char[][] rail = new char[numRows][len];

        // Keeps track of current row and direction for constructing the rail pattern
        int row = 0;
        boolean goingDown = true;

        // Iterates through each character in the plaintext
        for (int i = 0; i < len; i++) {

            // Place character in current row
            rail[row][i] = plaintext.charAt(i);

            // Change direction if we hit the top or bottom row
            if (row == 0) {
                goingDown = true;
            } else if (row == numRows - 1) {
                goingDown = false;
            }

            // Move to next row
            row += goingDown ? 1 : -1;
        }

        // Reads the rail matrix row by row starting from the specified starting row
        StringBuilder sb = new StringBuilder();
        boolean firstPass = true;
        for(int i = startingRow; !(i == startingRow && !firstPass); i++) {
            // Append all characters from the current row
            for(int j = 0; j < len; j++) {
                if(rail[i][j] != '\u0000') {
                    sb.append(rail[i][j]);
                }
            }
            // Loop back to the first row after reaching the last row
            if(i == numRows - 1) {
                firstPass = false;
                i = -1; // Will become 0 at next iteration
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {

        Scanner scnr = new Scanner(System.in);

        //System.out.println("----- RAIL FENCE CIPHER -----");
        //System.out.print("# Messages: ");

        int m = scnr.nextInt();

        for (int i = 0; i < m; i++) {
            //System.out.print("# Rows, Starting Row: ");
            int n = scnr.nextInt();
            int r = scnr.nextInt();
            //System.out.println("Plaintext:");
            String plaintext = scnr.next();
            //System.out.println("Ciphertext:");
            System.out.println(encrypt(plaintext, n, r-1));
        }

        scnr.close();

    }
}
