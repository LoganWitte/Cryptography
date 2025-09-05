import java.util.*;

public class AffineEncrypt {

    public static String affineEncrypt(String str, int a, int b) {

        int len = str.length();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            sb.append((char) (((str.charAt(i) - 'a') * a + b) % 26 + 'a'));
        }
        
        return sb.toString();
    }

    public static void main(String args[]) {

        Scanner scnr = new Scanner(System.in);

        System.out.print("Plaintext: ");
        String plaintext = scnr.next();
        
        System.out.print("a: ");
        int a = scnr.nextInt();

        System.out.print("b: ");
        int b = scnr.nextInt();

        scnr.close();

        System.out.println(affineEncrypt(plaintext, a, b));
    }
}
