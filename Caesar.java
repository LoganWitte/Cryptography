import java.util.*;

public class Caesar {
    
    public static char[] shift(String str, int shift) {
        int len = str.length();
        char[] output = new char[len];
	    for(int i = 0; i < len; i++) {
            output[i] = (char)((str.charAt(i) - 'a' + shift) % 26 + 'a');
	    }
	    return output;
    }

	public static void main(String[] args) {
	    
	    Scanner scnr = new Scanner(System.in);
	    
	    String input = "";
	    
	    System.out.print("Input: ");
	    input = scnr.next();

		scnr.close();
	    
	    for(int i = 1; i < 26; i++) {
	        System.out.println(shift(input, i));
	        System.out.println();
	    }
	    
	}
}