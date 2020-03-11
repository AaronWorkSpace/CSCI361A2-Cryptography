//Done by: Aaron Lim
//Student ID: 5985171
//Java version: 1.8.0_60

import java.util.*;
import java.io.*;
import java.math.*;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;

/*
1. A blockchain node wants to compute a new block using proof-of-work.

2. 
Hash function is obtained from an AES encryption in CBC mode, 
where the key is obtained from the first 128 bits of the message (which is the contents of the block). 
You can download the implementation of AES online, but you need to state where the source is from. The output of the hash is 128 bit, which is obtained from the final ciphertext.
*/
//AES CBC
//https://www.javainterviewpoint.com/aes-encryption-and-decryption/

public class task5{
	private static Scanner sc = new Scanner(System.in);
	private static ArrayList<String> ledgerPlainText = new ArrayList<String>();
	private static ArrayList<String> ledgerValidHash = new ArrayList<String>();
	private static ArrayList<String> outputValidNonce = new ArrayList<String>();
	private static ArrayList<String> outputValidPlainText = new ArrayList<String>();
	private static ArrayList<String> transValidMessage = new ArrayList<String>();
	static Cipher cipher;
	
	
	public static void main(String[] args) throws Exception{
		int nonceIV = 0;
		boolean ok = true;
		String choice = "";
		String ans = "";
		boolean check = true;
		String zero = "";
		int num = 0;
		String ledger = "new_ledger.txt";
		String output = "output.txt";
		String trans = "transaction.txt";
		String temp = "";
		
		try{
			Scanner S = new Scanner(new File(ledger));
			while(S.hasNext()){
				temp = S.nextLine();
				String[] arr = temp.split("\t");
				ledgerPlainText.add(arr[0]);
				ledgerValidHash.add(arr[1]);
			}
			S.close();
		}catch(Exception e){
			ok = false;
			System.out.println(ledger + " does not exist.");
		}
		
		try{
			Scanner S = new Scanner(new File(trans));
			while(S.hasNext()){
				temp = S.nextLine();
				transValidMessage.add(temp);
			}
			S.close();
		}catch(Exception e){
			ok = false;
			System.out.println(trans + " does not exist.");
		}
		
		try{
			Scanner S = new Scanner(new File(output));
			while(S.hasNext()){
				temp = S.nextLine();
				String[] arr = temp.split("\t");
				outputValidPlainText.add(arr[0]);
				outputValidNonce.add(arr[1]);
			}
			S.close();
		}catch(Exception e){
			ok = false;
			System.out.println(output + " does not exist.");
		}
		
		while(ok){
			System.out.print("Please input the number of leading zeros: ");
			ans = sc.nextLine();
			if(ans.length() == 0){
				System.out.println("Please input a valid digit");
			}
			else{
				check = checkDigit(ans);
				if(check){
					try{
						num = Integer.parseInt(ans);
						throw new Exception("demo");
					}catch(Exception e){
				
					}
					zero = leadingZero(num);
					ok = false;
				}
				else{
					System.out.println("Please input digit");
				}
			}
		}
		ok = true;
		while(ok){
			System.out.println("1. Add to ledger");
			System.out.println("2. Choice of print out");
			System.out.println("3. Quit");
			System.out.print("Choice: ");
			choice = sc.nextLine();
			if(choice.equals("1")){
				option1(zero, num);
			}
			else if(choice.equals("2")){
				if(ledgerPlainText.size() != 0){
					option2(zero, num);
				}
				else{
					System.out.println("The ledger is empty, please go to option 1");
				}
			}
			else if(choice.equals("3")){
				ok = false;
			}
			else{
				System.out.println("Please input a valid number");
				System.out.println();
			}
		}
		System.out.println("Thank you for using this program");	
	}
	
	public static void option1(String zero, int num) throws Exception{
		
		String msg = "";
		String temp = "";
		boolean ok = true;
		while(ok){
			System.out.print("Please input the transaction details: ");
			msg = sc.nextLine();
			if(msg.length() == 0){
				System.out.println("Please input your message");
			}
			else{
				ok = false;
			}
		}
		
		String block = generateKey(msg);
		
		byte[] secret = block.getBytes();
		SecretKey secretKey = new SecretKeySpec(secret, 0, secret.length, "AES");
		cipher = Cipher.getInstance("AES");
		boolean okay = true;
		byte[] prev = new byte[16];
		if(!ledgerValidHash.isEmpty()){
			prev = (ledgerValidHash.get(ledgerValidHash.size()-1)).getBytes();
		}
		else{
			okay = false;
			String strPrev = "0000123456789012";
			prev = strPrev.getBytes();
		}
		
		String prevHash = new String(prev);
		
		int counter = 0;
		String encoded = "";
		ok = true;
		String nonce = "0";
		
		String strIV = "1234567890123456";
		byte[] IV = strIV.getBytes();
		
		while(ok){
			String data = nonce + msg + prevHash;
			byte[] cipherText = encrypt(data.getBytes(),secretKey, IV);
			encoded = Base64.getEncoder().encodeToString(cipherText);
			if((encoded.substring(0, num)).equals(zero)){
				ok = false;
			}
			else{
				counter++;
				nonce = Integer.toString(counter);
			}
		}
		if(!okay){
			ledgerPlainText.add("Initial vector (not a transaction)");
			ledgerValidHash.add(prevHash);
		}
		ledgerPlainText.add(msg);
		ledgerValidHash.add(encoded);
		outputValidNonce.add(nonce);
		outputValidPlainText.add(msg);
		transValidMessage.add(msg);
		
		String s = "";
		
		for(int i = 0; i < ledgerValidHash.size(); i++){
			s += ledgerPlainText.get(i) + "\t" + ledgerValidHash.get(i) + "\n";
		}
		
		writeFile(s, "new_ledger.txt");
		
		s = "";
		for(int i = 0; i < outputValidNonce.size(); i++){
			s += outputValidPlainText.get(i) + "\t" + outputValidNonce.get(i) + "\n";
		}
		writeFile(s, "output.txt");
		
		s = "";
		for(int i = 0; i < transValidMessage.size(); i++){
			s += transValidMessage.get(i) + "\n";
		}
		
		writeFile(s, "transaction.txt");
		
		System.out.println();
	}
	
	public static void option2(String zero, int num) throws Exception{
		boolean ok = true;
		String choice = "";
		String n = "";
		String m = "";
		String h = "";
		String strIV = "1234567890123456";
		byte[] IV = strIV.getBytes();
		
		while(ok){
			System.out.println("1. Print ledger");
			System.out.println("2. Print output");
			System.out.println("3. Print Transaction");
			System.out.println("4. Verification");
			System.out.println("5. Back to main page");
			System.out.print("Choice: ");
			choice = sc.nextLine();
			if(choice.equals("1")){
				System.out.println(ledgerPlainText.get(0) + ", hash: " + ledgerValidHash.get(0));
				for(int i = 1; i < ledgerValidHash.size(); i++){
					System.out.println("Plain text: " + ledgerPlainText.get(i) + "\nhash: " + ledgerValidHash.get(i));
					System.out.println();
				}
			}
			else if(choice.equals("2")){
				for(int i = 0; i < outputValidNonce.size(); i++){
					System.out.println("Plain text: " + outputValidPlainText.get(i) + ", nonce: " + outputValidNonce.get(i));
				}
			}
			else if(choice.equals("3")){
				for(int i = 0; i < transValidMessage.size(); i++){
					System.out.println("Plain text: " + transValidMessage.get(i));
				}
			}
			else if(choice.equals("4")){
				System.out.print("Input the message: ");
				m = sc.nextLine();
				System.out.print("Input the Nonce: ");
				n = sc.nextLine();
				
				int x = ledgerPlainText.indexOf(m);
				if(x < 0){
					System.out.println("Invalid plaintext");
				}
				else{
					h = ledgerValidHash.get(x-1);
					String block = generateKey(m);
					byte[] secret = block.getBytes();
					SecretKey secretKey = new SecretKeySpec(secret, 0, secret.length, "AES");
					cipher = Cipher.getInstance("AES");
				
					String encoded = "";
		
					String data = n + m + h;
					byte[] cipherText = encrypt(data.getBytes(),secretKey, IV);
					encoded = Base64.getEncoder().encodeToString(cipherText);
				
					if(ledgerValidHash.contains(encoded)){
						if(encoded.substring(0, num).equals(zero)){
							System.out.println("Plain text and nonce are valid");
							System.out.println("Hashing for the transaction: " + encoded);
						}
						else{
							System.out.println("Fake transaction");
						}
					}
					else{
						System.out.println("Invalid nonce or a fake transaction");
					}
				}
			}
			else if(choice.equals("5")){
				ok = false;
			}
			else{
				System.out.println("Please input a valid number");
			}
			System.out.println();
		}
	}
	
	public static void writeFile(String s, String fileName){
		try{
			File f = new File(fileName);
			write(s, f);
		}catch(Exception e){
		
		}
	}
	
	public static void write(String s, File f) throws IOException{
		FileWriter fw = new FileWriter(f);
		fw.write(s);
		fw.close();
	}
	
	public static String leadingZero(int n){
		String zero = "";
		for(int i = 0; i < n; i++){
			zero += "0";
		}
		return zero;
	}
	
	public static String convertIntToBinary(int no){
		StringBuilder result = new StringBuilder();
		int i =0;
		while (no > 0){
			result.append(no%2);
			i++;
			no = no/2;
		}
		String val = (result.reverse()).toString();
		
		while(val.length() < 128){
			val = "0" + val;
		}
		
		return val;
	}

	public static String xor(String msg, String n){
		String sb = "";
		char msg1 = 'a';
		char msg2 = 'a';
		int counter = 0;
		
		while(counter < 128){
			if(msg1 == msg2){
				sb += "0";
			}
			else{
				sb += "1";
			}
			counter++;
		}
		return sb;
	}	
		
	public static String asciiToBinary(String asciiString){
		byte[] bytes = asciiString.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes)
		{
			int val = b;
			for (int i = 0; i < 8; i++)
			{
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		// binary.append(' ');
		}
		return binary.toString();
    	}
	
	public static byte[] encrypt (byte[] plaintext,SecretKey key,byte[] IV ) throws Exception
    {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        
        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        
        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        
        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);
        
        return cipherText;
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
	
	public static String generateKey(String data){
		String temp = "";
		for(int i = 0; i < data.length(); i++){
			if(data.length() >= 16){
				temp = data.substring(0, 16);
				data = data.substring(16);
			}
			else{
				temp = data;
			}
		}
		while(temp.length() < 16){
			temp = "0" + temp;
		}
		
		return temp;
	}
}
