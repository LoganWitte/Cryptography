// Logan Witte
// 11/27/25
// For homework 7 problem 1

public class H7P1 {

    // Given in problem
    static String ciphertext = "VXGVVDVDGVGVVGFDGFFGXGDXVGVVVGDFVXVGGVFFGDGVFVGGVXXVVGXDGGVGDXGGDFVXVGVGDFVFVXXFDXVXGGVDFGXVVFVFGGDVXGVGGGXDDGFVXFFGVGFFGFGXVVXXGXGFGVXFVVGVVDGDFVVDGDVXGVVDGX";
    static String keyword = "ELLIPTICCURVE";
    static char[][] grid = {
        {'K', 'L', 'T', 'E', 'V', 'O'},
        {'R', 'A', '1', '4', 'U', 'C'},
        {'S', 'Y', 'F', 'J', '7', 'P'},
        {'X', 'H', 'I', '5', 'D', 'W'},
        {'Q', '0', '6', '9', 'G', '3'},
        {'B', 'Z', 'N', '8', '2', 'M'},
    };
    //test data - from notes
    /*
    static String plaintext = "IAM28YEARSOLDNOW";
    static String keyword = "KNIGHTS";
    static char[][] grid = {
            { '0', 'O', 'H', '9', 'T', 'A' },
            { 'E', 'C', 'W', 'Z', 'V', '5' },
            { 'R', '8', '4', 'G', '2', 'I' },
            { 'K', 'J', 'U', 'X', 'P', 'Y' },
            { '6', 'S', 'B', 'N', 'D', 'Q' },
            { 'F', '3', 'M', '1', '7', 'L' }
    };
    */

    // Helper function - prints a 2d grid
    static void printGrid(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            char[] row = grid[i];
            for (int j = 0; j < row.length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Helper function - prints a 1d int array
    static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    // Helper function - returns order to reference columns
    // Based on alphabetic sort of keyword
    // Read left to right, each value is the column to reference
    // e.x. "CDBA" -> {3, 2, 0, 1}
    static int[] getAlphabeticOrder(String keyword) {
        int keywordLen = keyword.length();
        int[] alphabeticOrder = new int[keywordLen];
        boolean[] added = new boolean[keywordLen];
        // Adds 1 value to alphabeticOrder per cycle
        for (int i = 0; i < keywordLen; i++) {
            int min = Integer.MAX_VALUE;
            int index = -1;
            // Extracts minimum unused value from keyword
            for (int j = 0; j < keywordLen; j++) {
                if (added[j]) continue;
                int current = (int) keyword.charAt(j);
                if (current < min) {
                    min = current;
                    index = j;
                }
            }
            alphabeticOrder[i] = index;
            added[index] = true;
        }
        return alphabeticOrder;
    }

    // Helper function - returns char from grid based on row & col chars
    // Returns '_' if row/col char is invalid
    static char getGridChar(char row, char col, char[][] grid) {
        String ADFGVX = "ADFGVX";
        int rowInt = ADFGVX.indexOf(row);
        int colInt = ADFGVX.indexOf(col);
        if (rowInt == -1 || colInt == -1) {
            return '_';
        }
        //System.out.println("Getting grid char at (" + row + "," + col + ") -> (" + rowInt + "," + colInt + ") -> " + grid[rowInt][colInt]);
        return grid[rowInt][colInt];
    }

    // Helper function - returns grid row & col of a given char
    // Returns {'_', '_'} if char is not present
    static char[] getGridRowCol(char c, char[][] grid) {
        char[] ADFGVX = { 'A', 'D', 'F', 'G', 'V', 'X' };
        char row = '_';
        char col = '_';
        for (int i = 0; i < grid.length; i++) {
            char[] curRow = grid[i];
            for (int j = 0; j < curRow.length; j++) {
                if (curRow[j] == c) {
                    row = ADFGVX[i];
                    col = ADFGVX[j];
                }
            }
        }
        char[] output = { row, col };
        return output;
    }

    // Encrypts using ADFGVX cipher
    static String encrypt(String plaintext, String keyword, char[][] grid) {
        // First step: constructs y using grid
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            char[] rowAndCol = getGridRowCol(plaintext.charAt(i), grid);
            sb.append(rowAndCol[0]);
            sb.append(rowAndCol[1]);
        }
        String y = sb.toString();
        sb.setLength(0); // Clear StringBuilder
        System.out.println("Encryption y: " + y);

        // Second step: fills encryptionGrid left-to-right, top-to-bottom
        int gridWidth = keyword.length();
        int gridHeight = Math.ceilDiv(y.length(), gridWidth);
        System.out.println("Creating encrytionGrid of dimensions " + gridWidth + "x" + gridHeight);
        char[][] encryptionGrid = new char[gridHeight][gridWidth];
        boolean needToBreak = false;
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                if ((i * gridWidth + j) == y.length()) {
                    needToBreak = true;
                    break;
                } else {
                    // System.out.println("Referencing y[" + (i*gridWidth + j) + "]");
                    // System.out.println("Placing in encryptionGrid[" + i + "][" + j + "]");
                    encryptionGrid[i][j] = y.charAt(i * gridWidth + j);
                }
            }
            if (needToBreak == true)
                break;
        }
        System.out.println("Encryption grid:");
        printGrid(encryptionGrid);

        // Third step: reads encryptionGrid top-to-bottom, according to alphabetic order
        // of keyword
        int[] colOrder = getAlphabeticOrder(keyword);
        System.out.print("Column order: ");
        printArray(colOrder);
        for (int i = 0; i < gridWidth; i++) {
            int curCol = colOrder[i];
            for (int j = 0; j < gridHeight; j++) {
                char charToAdd = encryptionGrid[j][curCol];
                if (charToAdd != '\u0000') {
                    sb.append(charToAdd);
                }
            }
        }
        String ciphertext = sb.toString();
        System.out.println("Ciphertext: " + ciphertext);
        sb.setLength(0); // Clear StringBuilder
        return ciphertext;
    }

    // Decrypts using ADFGVX cipher
    static String decrypt(String ciphertext, String keyword, char[][] grid) {
        // First step: fills decryptionGrid top-to-bottom, according to alphabetic order of keyword
        int gridWidth = keyword.length();
        int gridHeight = Math.ceilDiv(ciphertext.length(), keyword.length());
        char[][] decryptionGrid = new char[gridHeight][gridWidth];
        int tallCols = ciphertext.length() % keyword.length();
        int[] colOrder = getAlphabeticOrder(keyword);
        int charsPlaced = 0;
        for (int i = 0; i < gridWidth; i++) {
            int curCol = colOrder[i];
            for (int j = 0; j < gridHeight; j++) {
                if (curCol >= tallCols && j == gridHeight - 1) {
                    // Short column, skip last row
                    break;
                }
                if (charsPlaced == ciphertext.length()) {
                    break;
                }
                decryptionGrid[j][curCol] = ciphertext.charAt(charsPlaced);
                charsPlaced++;
            }
        }
        System.out.println("Decryption grid:");
        printGrid(decryptionGrid);
        // Second step: reads decryptionGrid left-to-right, top-to-bottom
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                char charToAdd = decryptionGrid[i][j];
                if (charToAdd != '\u0000') {
                    sb.append(charToAdd);
                }
            }
        }
        String y = sb.toString();
        sb.setLength(0); // Clear StringBuilder
        System.out.println("Decryption y: " + y);
        // Third step: converts each letter pair to plaintext char using grid
        for (int i = 0; i < y.length(); i += 2) {
            char row = y.charAt(i);
            char col = y.charAt(i + 1);
            char charToAdd = getGridChar(row, col, grid);
            if (charToAdd != '_') {
                sb.append(getGridChar(row, col, grid));
            }
        }
        String plaintext = sb.toString();
        System.out.println("Plaintext: " + plaintext);
        return plaintext;
    }

    public static void main(String[] args) {
        System.out.println("--------------------------------------------------");
        System.out.println("H7P1");
        //System.out.println("Plaintext: " + plaintext);
        System.out.println("Keyword: " + keyword);
        printGrid(grid);
        //String testCiphertext = encrypt(plaintext, keyword, grid);
        decrypt(ciphertext, keyword, grid);
        System.out.println("--------------------------------------------------");
    }
}
