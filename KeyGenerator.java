import java.util.*;
public class KeyGenerator {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        System.out.print("Keyword: ");
        String keyword = scnr.next();
        scnr.close();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < keyword.length(); i++) {
            char ch = keyword.charAt(i);
            if(sb.indexOf(String.valueOf(ch)) == -1) {
                sb.append(ch);
            }
        }
        while(sb.length() < 25) {
            for(char ch = 'A'; ch <= 'Z'; ch++) {
                if(ch == 'J') continue; // Skip 'j'
                if(sb.indexOf(String.valueOf(ch)) == -1) {
                    sb.append(ch);
                    if(sb.length() == 25) break;
                }
            }
        }
        System.out.println("Generated Key (5x5 matrix):");
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                System.out.print(sb.charAt(i * 5 + j) + " ");
            }
            System.out.println();
        }
    }
}
