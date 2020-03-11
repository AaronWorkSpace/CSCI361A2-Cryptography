//Done by: Aaron Lim
//Student ID: 5985171
//Java version: 1.8.0_60

/*
In this assignment, you are to implement the Digital Signature Algorithm (DSA) as described in
the lecture notes. 

Your program needs to accept a file as an input, and it should produce sig.txt,
which is a signature on the input file. 

You should be able to verify the input file and the signature
in sig.txt and this should return True. Otherwise, return False.
*/

//NOT DONE

import java.io.*;
import java.util.*;
import java.math.*;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

//Assume file name: mssg.txt
//Assume mssg.txt only consist one value, which is the message

public class task4{
	private static Scanner sc = new Scanner(System.in);
	private static String algorithm = "DSA";
	
	public static void main(String[] args) throws Exception{
		String choice = "";
		boolean ok = true;
		boolean check = true;
		boolean ok2 = true;
		
		//one pair every section
		BigInteger publicKey = new BigInteger("0");
		BigInteger privateKey = new BigInteger("0");
		BigInteger p = new BigInteger("0");
		BigInteger q = new BigInteger("0");
		BigInteger g = new BigInteger("0");
		BigInteger h = new BigInteger("0");
		BigInteger one = new BigInteger("1");
		BigInteger two = new BigInteger("2");
		BigInteger bigTemp = new BigInteger("0");
		BigInteger multi = new BigInteger("30000000");
		BigInteger fiveOneTwo = new BigInteger("512");
		BigInteger oneZeroTwoFour = new BigInteger("1024");
		BigInteger sixFour = new BigInteger("64");
		
		Random rand = new Random();
		int l = 0;
		ArrayList<BigInteger> primeFactor = new ArrayList<BigInteger>();
		
		//generate public
		//generate q > p
		while(ok){
			q = BigInteger.probablePrime(160, rand);
			if(q.isProbablePrime(1)){
				ok2 = true;
				while(ok2){
					multi = two.pow((rand.nextInt(512)) + 512);
					p = (q.multiply(multi)).add(BigInteger.ONE);
					if(p.isProbablePrime(1)){
						if((two.pow(1024)).compareTo(p) == 1){
							if(((p.divide(two)).mod(sixFour)).equals(BigInteger.ZERO)){
								ok = false;
								ok2 = false;
							}
						}
					}
					else if((two.pow(1024)).compareTo(p) == -1){
						ok2 = false;
					}
				}
			}
		}
		
		bigTemp = p.subtract(BigInteger.ONE);
		
		//generate g and h
		ok = true;
		while(ok){
			h = randP(bigTemp, rand);
			g = h.modPow(bigTemp.divide(q), p);
			if(g.compareTo(BigInteger.ZERO) == 1){
				ok = false;
			}
		}
		
		System.out.println("p: " + p);
		System.out.println("q: " + q);
		System.out.println("g: " + g);
		System.out.println();
		
		BigInteger sk = new BigInteger("0");
		BigInteger pk = new BigInteger("0");
		
		sk = randP(q, rand);
		pk = g.modPow(sk, p);
		
		System.out.println("Key X: " + sk);
		System.out.println("Key Y: " + pk);
		
		ok = true;
		while(ok){
			System.out.println("1. Input file name");
			System.out.println("2. Verify the file");
			System.out.println("3. Quit");
			System.out.print("Enter your choice: ");
			choice = sc.nextLine();
			
			if(choice.equals("1")){
				System.out.println();
				option1(p, g, q, sk);
				System.out.println();
			}
			
			else if(choice.equals("2")){
				check = checkFile("sig.txt");
				System.out.println();
				if(check){
					option2(p, g, q, pk);
				}
				else{
					System.out.println("Please proceed to 1 to create sig.txt.");
				}
				System.out.println();
			}
			
			else if(choice.equals("3")){
				deleteFile("sig.txt");
				ok = false;
			}
			
			else{
				System.out.println("Please input a valid number, 1/2/3.");
			}
		}
		System.out.println();
		System.out.println("Thank you for using this program.");
	}
	
	public static void option1(BigInteger p, BigInteger g, BigInteger q, BigInteger sk) throws Exception{
		deleteFile("sig.txt");
		String fileName = "";
		String msg = "";
		
		boolean ok = true;
		boolean check = true;
		
		while(ok){
			System.out.print("Input message file name: ");
			fileName = sc.nextLine();
			check = checkFile(fileName);
			if(check){
				ok = false;
			}
			else{
				System.out.println(fileName + " does not exist.");
			}
		}

		try{
			Scanner S = new Scanner(new File(fileName));
			while(S.hasNext()){
				msg += S.next();
				if(S.hasNext()){
					msg += " ";
				}
			}
			S.close();
		}catch(Exception ex){
	
		}
		
		Random rand = new Random();
		
		ok = true;
		BigInteger k = new BigInteger("0");
		BigInteger inverseK = new BigInteger("0");
		while(ok){
			k = randP(p, rand);
			check = checkInverse(q, k);
			if(check){
				inverseK = getInverse(q, k);
				ok = false;
			}
		}
		BigInteger r = (g.modPow(k, p)).mod(q);
		String hashMsg = encryptThisString(msg);
		int hashInt = 0;
		for(int i = 0; i < hashMsg.length(); i++){
			hashInt += Integer.parseInt(Character.toString(hashMsg.charAt(i)), 16);
		}
		BigInteger hash = BigInteger.valueOf(hashInt);
		BigInteger s = ((inverseK.multiply(hash.add(sk.multiply(r)))).mod(q));
		
		String sign = s + " " + r;
		
		sigFile(sign, "sig.txt");
	}
	
	public static void option2(BigInteger p, BigInteger g, BigInteger q, BigInteger pk) throws Exception{
		String sigFileName = "sig.txt";
		String msgFileName = "";
		String sign = "";
		String R = "";
		String H = "";
		
		boolean check = true;
		
		boolean ok = true;
		while(ok){
			System.out.print("Please input the signature file: ");
			sigFileName = sc.nextLine();
			check = checkFile(sigFileName);
			if(check){
				ok = false;
			}
			else{
				System.out.println(sigFileName + " does not exist, please try again");
			}
		}
		
		ok = true;
		while(ok){
			System.out.print("Please input the message file: ");
			msgFileName = sc.nextLine();
			check = checkFile(msgFileName);
			if(check){
				ok = false;
			}
			else{
				System.out.println(msgFileName + " does not exist, please try again");
			}
		}
		
		try{
			Scanner S = new Scanner(new File(sigFileName));
			while(S.hasNext()){
				sign += S.next();
				R += S.next();
			}
			S.close();
		}catch(Exception ex){
	
		}
		
		String msg = "";
		try{
			Scanner S = new Scanner(new File(msgFileName));
			while(S.hasNext()){
				msg += S.next();
				if(S.hasNext()){
					msg += " ";
				}
			}
			S.close();
		}catch(Exception ex){
	
		}
		
		String hashMsg = encryptThisString(msg);
		int hashInt = 0;
		for(int i = 0; i < hashMsg.length(); i++){
			hashInt += Integer.parseInt(Character.toString(hashMsg.charAt(i)), 16);
		}
		BigInteger hash = BigInteger.valueOf(hashInt);
		
		
		BigInteger s = new BigInteger(sign);
		BigInteger r = new BigInteger(R);
		if((r.compareTo(q) == -1) && (r.compareTo(BigInteger.ZERO) == 1) && (s.compareTo(q) == -1) && (s.compareTo(BigInteger.ZERO) == 1)){
			BigInteger w = getInverse(q, s);
			BigInteger u1 = (hash.multiply(w)).mod(q);
			BigInteger u2 = (r.multiply(w)).mod(q);
			BigInteger v = (((g.modPow(u1, p)).multiply(pk.modPow(u2, p))).mod(p)).mod(q);
			if(v.compareTo(r) == 0){
				System.out.println("True");
			}
			else{
				System.out.println("False");
			}
		}
		else{
			System.out.println("False");
		}
		
		//check = verification(msg, signature, pk);
	}
	
	
	public static void sigFile(String b, String fileName){
		try{
			File f = new File(fileName);
			String s = b;
			write(s, f);
			System.out.println(fileName + " created");
		}catch(Exception e){
		
		}
	}
	
	public static void write(String s, File f) throws IOException{
		FileWriter fw = new FileWriter(f);
		fw.write(s);
		fw.close();
	}
	
	public static boolean checkFile(String file){
		File f = new File(file);
		if(f.exists()){
			return true;
		}
		return false;
	}
	
	public static void deleteFile(String fileName){
		File file = new File(fileName);
		if(file.delete()){
			System.out.println(fileName + " is deleted.");
		}
	}
	
	public static boolean checkMulti(int a, int b){
		for(int i = 1; i <= 16; i++){
			if(a == (b*i)){
				return true;
			}
		}
		return false;
	}
	
	
	public static BigInteger getMax(int l){
		BigInteger max = new BigInteger("1");
		BigInteger two = new BigInteger("2");
		BigInteger total = max;
		
		for(int i = 1; i < l; i++){
			total = total.multiply(two);
			max = max.add(total);
		}
		return max;
	}
	
	public static BigInteger randP(BigInteger maxP, Random rand){
		int maxBit = maxP.bitLength();
		BigInteger returnValue = new BigInteger(maxBit, rand);
		
		return returnValue;
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
			
			// return the HashText
			return hashtext; 
		
		// For specifying wrong message digest algorithms	
		}catch (NoSuchAlgorithmException e) { 
			throw new RuntimeException(e); 
		} 
	} 
	
	public static BigInteger getInverse(BigInteger n1, BigInteger n2){
		BigInteger q = new BigInteger("0");
		BigInteger r = new BigInteger("0");
		BigInteger a1 = new BigInteger("1");
		BigInteger a2 = new BigInteger("0");
		BigInteger b1 = new BigInteger("0");
		BigInteger b2 = new BigInteger("1");
		BigInteger tempA = new BigInteger("0");
		BigInteger tempB = new BigInteger("0");
		BigInteger temp = n1;
		BigInteger zero = new BigInteger("0");
		
		boolean ok = true;

		//2nd and so on
		while(ok){
			q = n1.divide(n2);
			r = n1.mod(n2);
			n1 = n2;
			n2 = r;
			tempA = a2;
			tempB = b2;
			a2 = a1.subtract(a2.multiply(q));
			b2 = b1.subtract(b2.multiply(q));
			a1 = tempA;
			b1 = tempB;
			
			if(r.compareTo(zero) == 0){
				ok = false;
			}
		}
		
		//give -1 if b1 < 0
		if(b1.compareTo(zero) == -1){
			b1 = b1.add(temp);
		}
		
		return b1;
	}
	
	public static boolean checkInverse(BigInteger n1, BigInteger n2){
		BigInteger temp = new BigInteger("0");
		if((n2.compareTo(n1)) == 1){
			temp = n2;
			n2 = n1;
			n1 = temp;
			
		}

		BigInteger q = new BigInteger("0");
		BigInteger r = new BigInteger("0");
		BigInteger a1 = new BigInteger("1");
		BigInteger a2 = new BigInteger("0");
		BigInteger b1 = new BigInteger("0");
		BigInteger b2 = new BigInteger("1");
		BigInteger tempA = new BigInteger("0");
		BigInteger tempB = new BigInteger("0");
		temp = n1;
		
		boolean ok = true;
		boolean check = true;
		
		//2nd and so on
		while(ok){
			q = n1.divide(n2);
			r = n1.mod(n2);
			n1 = n2;
			n2 = r;
			tempA = a2;
			tempB = b2;
			a2 = a1.subtract(a2.multiply(q));
			b2 = b1.subtract(b2.multiply(q));
			a1 = tempA;
			b1 = tempB;
			
			if((r.compareTo(BigInteger.ONE)) == 0){
				check = true;
				ok = false;
			}
			else if((r.compareTo(BigInteger.ZERO)) == 0){
				check = false;
				ok = false;
			}
		}
		return check;
	}
}
