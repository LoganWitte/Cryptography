// Logan Witte, 10/19/25, CIS 3362

import java.math.BigInteger;

public class H5P6 {

	// Fast Modular Exponentiation. 
	// Brought from ModExp.java in the course repository.
	public static BigInteger modExpRec(BigInteger base, BigInteger exp, BigInteger n) {
		
		BigInteger zero = new BigInteger("0");
		BigInteger one = new BigInteger("1");
		BigInteger two = one.add(one);
		
		// Get base cases out of the way.
		if (exp.equals(zero))
			return one;
		if (exp.equals(one))
			return base.mod(n);
			
		// Here is where we get our key savings.	
		if (exp.mod(two).equals(zero)) {
			
			// Just solve for the square root of the answer.
			BigInteger ans = modExpRec(base, exp.divide(two), n);
			
			// Save yourself a great deal of work by REUSING the
			// result of the square root, so to speak.
			return (ans.multiply(ans)).mod(n);
		}	
		
		// Here's the other case which works in the typical way. It's
		// okay because the subsequent call is guaranteed to have an
		// even exponent.
		return (base.multiply(modExpRec(base,exp.subtract(one),n))).mod(n);
	}

	public static void main(String[] args) {
	    
	    int[] frequencies = new int[23];
	    
		for(int a = 1; a < 35; a++) {
		    for(int m = 1; m <= 24; m++) {
		        int result = modExpRec(BigInteger.valueOf(a), BigInteger.valueOf(m), BigInteger.valueOf(35)).intValue();
		        if(result == 1) {
		            if(m == 24) {
		                // This will only trigger if a primitive root of 35 is found (never)
		                System.out.println("PRIMITIVE ROOT FOUND: " + a);
		                break;
		            }
		            frequencies[m-1] ++;
		            break;
		        }
		    }
		}
		
		System.out.println("Order(M)\t# Occurences");
		for(int i = 0; i < 23; i++) {
		    System.out.print("M=" + (i+1) + ":\t\t" + frequencies[i] + " ");
		    for(int j = 0; j < frequencies[i]; j++) {
		        System.out.print("X");
		    }
		    System.out.println();
		}
	}
}