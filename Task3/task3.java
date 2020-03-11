//Done by: Aaron Lim
//Student ID: 5985171
//Java version: 1.8.0_60

import java.io.*;
import java.util.*;
import java.math.BigInteger;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

/*
SHA-1 is a commonly used hash function. It produces 160-bit hash value. You can download the
source code for SHA-1 from the web, but you need to state where you get the original code from. As
an example, see this github: https://github.com/clibs/sha1
We learnt in the lecture that a good hash function should be collision-resistant, meaning that it
is difficult to find two messages, m and m', where m 6= m0, such that H(m) = H(m). In this task,
we assume a simplified version of SHA-1, named SSHA-1, is used for hashing. SSHA-1 only outputs
the first 36 bits of SHA-1 when hashing a message.
Your task is to find a pair of integers (x; x0) such that x 6= x0 but the SSHA-1 hash values of the
following two messages are the same. 
The Cat-In-The-Hat owes [FIRSTNAME] x dollars
The Cat-In-The-Hat owes [FIRSTNAME] x' dollars
You should replace [FIRSTNAME] with your first name. Write a C++ or JAVA program to
accomplish the task. Your program should output the two messages, their hash values (should be
the same), and the number of trials your program has made before it finds the collision.
*/

//https://www.geeksforgeeks.org/sha-1-hash-in-java/
public class task3{
	public static void main(String[] args) throws NoSuchAlgorithmException{
		String msg1 = "";
		String encryptedMsg1 = "";
		
		char checkChar1 = 'a';
		char checkChar2 = 'a';
		
		int counter = 0;
		boolean ok = true;
		int x = 0;
		
		msg1 = "The Cat-In-The-Hat owes Aaron " + x + " dollars";
		encryptedMsg1 = encryptThisString(msg1);
		ArrayList<String> col = new ArrayList<String>();
		col.add(encryptedMsg1);
		while(ok){
			x++;
			counter++;
			msg1 = "The Cat-In-The-Hat owes Aaron " + x + " dollars";
			encryptedMsg1 = encryptThisString(msg1);
			
			if(col.contains(encryptedMsg1)){
				ok = false;
				x = col.indexOf(encryptedMsg1);
				System.out.println("X: " + x);
				msg1 = "The Cat-In-The-Hat owes Aaron " + x + " dollars";
				encryptedMsg1 = encryptThisString(msg1);
				System.out.println("SSHA-1 hashing collision when x: " + encryptedMsg1);
				System.out.println("X': " + counter);
				msg1 = "The Cat-In-The-Hat owes Aaron " + counter + " dollars";
               		 	encryptedMsg1 = encryptThisString(msg1);
                		System.out.println("SSHA-1 hashing collision when x': "  + encryptedMsg1);
			}
			else{
				col.add(encryptedMsg1);
			}
		}
	}
	
	public static String encryptThisString(String input){ 
		try{ 
			// getInstance() method is called with algorithm SHA-1 
			MessageDigest md = MessageDigest.getInstance("SHA-1"); 

			// digest() method is called 
			// to calculate message digest of the input string 
			// returned as array of byte 
			byte[] messageDigest = md.digest(input.getBytes()); 

			// Convert byte array into signum representation 
			BigInteger no = new BigInteger(1, messageDigest); 

			// Convert message digest into hex value 
			String hashtext = no.toString(16); 

			// Add preceding 0s to make it 32 bit 
			while (hashtext.length() < 32) { 
				hashtext = "0" + hashtext; 
			} 
			
			//ssha-1, edit the substring to get what you want
			hashtext = hashtext.substring(0,9);
			
			// return the HashText
			return hashtext; 
		
		// For specifying wrong message digest algorithms	
		}catch (NoSuchAlgorithmException e) { 
			throw new RuntimeException(e); 
		} 
	} 
}
