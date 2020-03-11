//Done by: Aaron Lim
//Student ID: 5985171
//Java version: 1.8.0_60

import java.io.*;
import java.util.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class task1{
	public static Scanner sc = new Scanner(System.in);
	
	public static void main(String [] args){
		boolean ok = true;
		boolean check = true;
		String choice = "";
		while(ok){
			System.out.println("1. RSA key generation");
			System.out.println("2. RSA signing function");
			System.out.println("3. RSA verification function");
			System.out.println("4. Quit");
			System.out.println();
			System.out.print("Please enter your choice: ");
			choice = sc.nextLine();
			if(choice.equals("1")){
				rsaGen();
			}
			else if(choice.equals("2")){
				check = true;
				check = checkFile("sk.txt");
				if(!check){
					System.out.println();
					System.out.println("Please proceed to 1, creation of RSA key");
					System.out.println();
				}
				else{
					rsaSign();
				}
			}
			else if(choice.equals("3")){
				check = true;
				check = checkFile("pk.txt");
				if(!check){
					System.out.println();
					System.out.println("Please proceed to 1, creation of RSA key");
					System.out.println();
				}
				else{
					check = checkFile("sig.txt");
					if(!check){
						System.out.println();
						System.out.println("Please proceed to 2, creation of signature");
						System.out.println();
					}
					else{
						rsaVerify();
					}
				}
			}
			else if(choice.equals("4")){
				ok = false;
				//deletion of file not done
				//file to delete: pk.txt, sk.txt, sig.txt
				deleteFile("pk.txt");
				deleteFile("sk.txt");
				deleteFile("sig.txt");
				
				System.out.println("Thank you for using this program.");
			}
			else{
				System.out.println("Please try again, please enter only 1/2/3/4.");
			}
		}
	}
	
	public static void deleteFile(String fileName){
		File file = new File(fileName);
		if(file.delete()){
			System.out.println(fileName + " is deleted.");
		}
	}
	
	public static void rsaGen(){
		deleteFile("pk.txt");
		deleteFile("sk.txt");
		deleteFile("sig.txt");
	
		boolean ok = true;
		boolean check = true;
		boolean checkPrime = true;
		
		int bit = 0;
		BigInteger p = new BigInteger("0");
		BigInteger q = new BigInteger("0");
		BigInteger n = new BigInteger("0");
		BigInteger phiN = new BigInteger("0");
		BigInteger d = new BigInteger("0");
		BigInteger e = new BigInteger("0");
		BigInteger one = new BigInteger("1");
		
		Random rand = new Random();
		int compare = 0;
		String temp = "";
		char checkChar = 'a';
		int tempInt = 0;
		int bit2 = 0;
		String temp2 = "";
		while(ok){
			check = true;
			//2 cannot be bit, because 3 and 1 is the only option for d and e
			//3 cannot be bit, because all inverse of e is 1
			System.out.print("Bit length P(Up to 32 bit): ");
			temp = sc.nextLine();
			
			check = checkDigit(temp);
			
			if(!check){
				System.out.println("You have entered a invalid string, please try again");
			}
			
			else{
				if(temp.length() != 0){
					bit = Integer.parseInt(temp);
					if(bit > 1 && bit <= 32){
						//generate p
						while(check){
							checkPrime = true;
							p = generatePQ(bit, rand);
							checkPrime = returnPrime(p);
							if(checkPrime){
								check = false;
							}
						}
						check = true;
						while(check){
							System.out.print("Bit length Q(Up to 32 bit): ");
							temp2 = sc.nextLine();
							if(temp2.length() != 0){
								if(!temp2.equals(temp)){
									bit2 = Integer.parseInt(temp2);
									if(bit2 > 1 && bit2 <= 32){
										check = false;
									}
									else{
										System.out.println("Please input valid number, 2 ~ 32");
									}
								}
								else{
									System.out.println("Please input another bit, p cannot be same as q");
								}
							}
							else{
								System.out.println("input a valid number");
							}
						}
						
						check = true;
						//generate q
						while(check){
							checkPrime = true;
							q = generatePQ(bit2, rand);
							checkPrime = returnPrime(q);
							compare = q.compareTo(p);
							if(compare != 0 && checkPrime){
								check = false;
							}
						}
				
						//generate n
						n = p.multiply(q);
				
						//generate phiN
						phiN = (p.subtract(one)).multiply(q.subtract(one));
				
						check = true;
				
						while(check){
							checkPrime = true;
							do{
								e = new BigInteger(phiN.bitLength(), rand);
							}while(e.compareTo(one) <= 0 || e.compareTo(phiN) >= 0 || !e.gcd(phiN).equals(one));
							checkPrime = returnPrime(e);
							if(checkPrime){
								d = getInverse(phiN, e);
								check = false;
							}
						}

						ok = false;
					}
					else{
						System.out.println("Please enter a digit which is 4 ~ 32).");
					}
				}
				else{
					System.out.println("Please enter a digit which is 2 ~ 32).");
				}
			}
		}
		//copy the info to file
		kFile(n, e, "pk.txt");
		kFile(n, d, "sk.txt");
	
		//printing to command prompt
		System.out.println();
		System.out.println("P: " + p);
		System.out.println("Q: " + q);
		System.out.println("N: " + p + "*" + q + " = " + n);
		System.out.println("phi(N): " + p.subtract(one) + "*" + q.subtract(one) + " = " + phiN);
		System.out.println("e: " + e);
		System.out.println("d: " + d);
		System.out.println("Public key: (" + n + ", " + e + ")");
		System.out.println("Private key: (" + n + ", " + d + ")");
		System.out.println();
	}
	
	public static BigInteger generatePQ(int bit, Random rand){
		BigInteger returnValue = new BigInteger("0");
		
		if(bit >= 8){
			returnValue = BigInteger.probablePrime(bit-1, rand);
		}
		else{
			returnValue = BigInteger.probablePrime(bit, rand);
		}
		
		return returnValue;
	}
	
	public static void kFile(BigInteger n, BigInteger de, String fileName){
		try{
			File f = new File(fileName);
			String s = n.toString() + " " + de.toString();
			write(s, f);
		}
		catch(Exception e){
		
		}
	}
	
	public static void sigFile(BigInteger s, String fileName){
		try{
			File f = new File(fileName);
			String x = s.toString();
			write(x, f);
		}catch(Exception e){
		
		}
	}
	
	public static void write(String s, File f) throws IOException{
		FileWriter fw = new FileWriter(f);
		fw.write(s);
		fw.close();
	}
	
	public static boolean returnPrime(BigInteger number) {
		//check via BigInteger.isProbablePrime(certainty)
		if (!number.isProbablePrime(5))
			return false;

		//check if even
		BigInteger two = new BigInteger("2");
			if (!two.equals(number) && BigInteger.ZERO.equals(number.mod(two)))
				return false;

		//find divisor if any from 3 to 'number'
		for (BigInteger i = new BigInteger("3"); i.multiply(i).compareTo(number) < 1; i = i.add(two)) { //start from 3, 5, etc. the odd number, and look for a divisor if any
			if (BigInteger.ZERO.equals(number.mod(i))) //check if 'i' is divisor of 'number'
		   		return false;
		}
		return true;
	}
	
	public static boolean checkDigit(String temp){
		char checkChar = 'a';
		boolean check = true;
		for(int i = 0; i < temp.length(); i++){
			checkChar = temp.charAt(i);
			if(!Character.isDigit(checkChar)){
				check = false;
			}
		}
		return check;
	}
	
	public static BigInteger stringToBig(String temp){
		BigInteger p = new BigInteger("0");
		try{
			p = new BigInteger(temp);
			throw new Exception("demo");
		}catch(Exception e){
			
		}
		return p;
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
	
	public static boolean checkFile(String file){
		File f = new File(file);
		if(f.exists()){
			return true;
		}
		return false;
	}
	
	public static boolean checkWithinPhiN(BigInteger e, BigInteger phiN, BigInteger one){
		//check if e is within 1 =< e >= phiN - 1
		int result1 = e.compareTo(one); //should give 1
		int result2 = e.compareTo(phiN); //should give -1
		
		if(result1 == 1 && result2 == -1){
			return true;
		}
		return false;
	}
	
	public static void rsaSign(){
		boolean ok = true;
		String skFileName = "sk.txt";
		String msgFileName = "mssg.txt";
		
		BigInteger d = new BigInteger("0");
		BigInteger n = new BigInteger("0");
		BigInteger msg = new BigInteger("0");
		BigInteger s = new BigInteger("0");
		
		String temp = "";
		boolean check = true;
		
		while(ok){
			System.out.print("Please input the secret key file: ");
			skFileName = sc.nextLine();
			check = checkFile(skFileName);
			if(check){
				ok = false;
			}
			else{
				System.out.println(skFileName + " does not exist, please try again");
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
		ok = true;
		try{
			Scanner S = new Scanner(new File(skFileName));
			while(S.hasNext()){
				temp = S.next();
				n = new BigInteger(temp);
				
				temp = S.next();
				d = new BigInteger(temp);
			}
			S.close();
		}catch(Exception e){
			ok = false;
			System.out.println(skFileName + " does not exist.");
		}
		
		try{
			Scanner S = new Scanner(new File(msgFileName));
			while(S.hasNext()){
				temp = S.next();
				msg = new BigInteger(temp);
			}
			S.close();
		}catch(Exception e){
			ok = false;
			System.out.println(msgFileName + " does not exist.");
		}
		System.out.println();
		if(ok){
			s = msg.modPow(d, n);
			//create file for signature
			sigFile(s, "sig.txt");
			
			//print on command prompt
			System.out.println("Signature = " + msg + "^" + d + " mod " + n + " = " + s);
		}
		else{
			System.out.println("One of the file does not exist, please try again.");
		}
		System.out.println();
	}
	
	public static void rsaVerify(){
		boolean ok = true;
		BigInteger s = new BigInteger("0");
		BigInteger n = new BigInteger("0");
		BigInteger e = new BigInteger("0");
		BigInteger msg = new BigInteger("0");
		BigInteger oMsg = new BigInteger("0");
		
		String temp = "";
		String pkFileName = "pk.txt";
		String sigFileName = "sig.txt";
		String msgFileName = "mssg.txt";
		boolean check = true;
		ok = true;
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
			System.out.print("Please input the public key file: ");
			pkFileName = sc.nextLine();
			check = checkFile(pkFileName);
			if(check){
				ok = false;
			}
			else{
				System.out.println(pkFileName + " does not exist, please try again");
			}
		}
		
		ok = true;
		try{
			Scanner S = new Scanner(new File(pkFileName));
			while(S.hasNext()){
				temp = S.next();
				n = new BigInteger(temp);
				
				temp = S.next();
				e = new BigInteger(temp);
			}
			S.close();
		}catch(Exception ex){
			ok = false;
			System.out.println(pkFileName + " does not exist.");
		}
		
		try{
			Scanner S = new Scanner(new File(sigFileName));
			while(S.hasNext()){
				temp = S.next();
				s = new BigInteger(temp);
			}
			S.close();
		}catch(Exception ex){
			ok = false;
			System.out.println(sigFileName + " does not exist.");
		}
		
		try{
			Scanner S = new Scanner(new File(msgFileName));
			while(S.hasNext()){
				temp = S.next();
				oMsg = new BigInteger(temp);
			}
			S.close();
		}catch(Exception ex){
			ok = false;
			System.out.println(msgFileName + " does not exist.");
		}
		
		System.out.println();
		
		boolean ok2 = true;
		String m = "";
		while(ok2){
			System.out.print("Please input the message M: ");
			m = sc.nextLine();
			if(m.length() != 0){
				ok2 = false;
			}
		}
		
		
		
		if(ok){
			msg = s.modPow(e, n);
			String M = msg.toString();
			if(M.equals(m)){
				System.out.println("True");
			}
			else{
				System.out.println("False");
			}
		}
		else{
			System.out.println("One of the file does not exist, please try again.");
		}
		System.out.println();
	}
}
