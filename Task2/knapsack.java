//Done by: Aaron Lim
//Student ID: 5985171
//Java version: 1.8.0_60

import java.io.*;
import java.util.*;

/*

In this section, you are to implement a trapdoor knapsack encryption scheme. When the program
is run, 

it first asks for the size of the super-increasing knapsack, t
hen the user is asked to enter the value of each ai in the private key. 
Then, the user is asked to enter a modulus, follows by a multiplier.

You will need to check whether condition of the multipllier is satisfied. Then, the public key will
be generated and shown. 

Now, a set of message is being asked, and the ciphertext will need to be
displayed. 

Finally, a ciphertext will need to be asked and the correct decryption of the ciphertext
will need to be displayed. Implement this part as knapsack.cpp or knapsack.java

*/

public class knapsack{
	private static int [] superIncreasing = new int[1];
	private static int inverseValue = 0;
	private static int [] knapsack = new int[1];
	private static int mod = 0;
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args){
		boolean ok = true;
		int multi = 0;
		boolean check = true;
		String temp = "";
		int size = 0;
		int tempInt = 0;
		String msg = "";
		boolean checkP = true;
		char checkChar = 'a';
		int charInt = 0;
		String[] msgBinary = new String[1];
		
		//requesting size
		while(ok){
			check = true;
			System.out.print("Please input the size of the super-increasing knapsack: ");
			temp = sc.nextLine();
			if(temp == ""){
				System.out.println("Please enter some digits");
			}
			check = checkDigit(temp);
			if(check){
				try{
					size = Integer.parseInt(temp);
					throw new Exception("demo");
				}catch(Exception e){
					
				}
				if(size < 2){
					System.out.println("Please enter digits above 2");
				}
				else{
					ok = false;
				}
			}
			else{
				System.out.println("Please input a valid digit");
			}
		}
		
		knapsack = new int[size];
		superIncreasing = new int[size];
		int total = 0;
		int inputValue = 0;
		
		//requesting digits (super increasing)
		System.out.println();
		for(int i = 0; i < size; i++){
			ok = true;
			while(ok){
				total = 0;
				tempInt = i+1;
				System.out.print("Please input number " + tempInt  + " value: ");
				temp = sc.nextLine();
				
				if(temp == " "){
					System.out.println("Please input a valid number");
				}
				else{
					check = checkDigit(temp);
					if(check && temp.length() >= 1){
						try{
							tempInt = Integer.parseInt(temp);
							throw new Exception("demo");
						}catch(Exception e){
					
						}
						for(int j = 0; j < inputValue; j++){
							total += superIncreasing[j];
						}
						check = checkBigger(superIncreasing, tempInt, i + 1);
						if(check){
							superIncreasing[i] = tempInt;
							ok = false;
							inputValue++;
						}
						else{
							System.out.println("The value you have input is too small, please input value which is greater than " + total);
						}
					}
					else{
						System.out.println("Please input a valid digit");
					}
				}
			}
		}
		total = 0;
		for(int j = 0; j < size; j++){
			total += superIncreasing[j];
		}
		
		//modulus, need to be bigger than superincreasing and a prime
		System.out.println();
		ok = true;
		while(ok){
			System.out.print("Please input the modulus value: ");
			temp = sc.nextLine();
			check = checkDigit(temp);
			if(check){
				try{
					tempInt = Integer.parseInt(temp);
					throw new Exception("demo");
				}catch(Exception e){
				
				}
				checkP = checkPrime(tempInt);
				if(tempInt > total && checkP){
					ok = false;
					mod = tempInt;
				}
				else{
					System.out.println("Please input a value greater than " + total + " and a prime value");
				}
			}
			else{
				System.out.println("Please input a valid digit");
			}
		}
		//need relative prime to mod
		System.out.println();
		boolean checkMulti = true;
		ok = true;
		while(ok){
			System.out.print("Please input the multiplier: ");
			temp = sc.nextLine();
			check = checkDigit(temp);
			if(check){
				try{
					tempInt = Integer.parseInt(temp);
					throw new Exception("demo");
				}catch(Exception e){
				
				}
				if(tempInt == 1){
					System.out.println("Please input other value other than 1 and below");
				}
				else if(tempInt >= mod){
					System.out.println("Please input another value, multiplier cannot be equal or bigger than the mod");	
				}
				else{
					check = checkInverse(mod, tempInt);
					if(check){
						inverseValue = getInverse(mod, tempInt);
						multi = tempInt;
						ok = false;
					}
					else{
						System.out.println("Greatest common factor of " + tempInt + " and " + mod + " is not 1, please input another value");
					}
				}
			}
			else{
				System.out.println("Please input a valid digit");
			}
		}
		
		/*
		Now, a set of message is being asked, and the ciphertext will need to be
		displayed. 

		Finally, a ciphertext will need to be asked and the correct decryption of the ciphertext
		will need to be displayed. Implement this part as knapsack.cpp or knapsack.java
		*/
		
		System.out.print("Public key: ");
		for(int i = 0; i < size; i++){
			tempInt = superIncreasing[i];
			knapsack[i] = (tempInt * multi) % mod;
			System.out.print(knapsack[i]);
			if(i != size-1){
				System.out.print(", ");
			}
		}
		System.out.println();
		System.out.print("Private key: ");
		for(int i = 0; i < size; i++){
			System.out.print(superIncreasing[i]);
			if(i != size-1){
				System.out.print(", ");
			}
		}
		System.out.println();
		
		System.out.println(multi + "^-1 mod " + mod + ": " + inverseValue);
		System.out.println();
		ok = true;
		String choice = "";
		while(ok){
			System.out.println("1. Encrypt");
			System.out.println("2. Decrypt");
			System.out.println("3. Quit");
			System.out.print("Choice: ");
			choice = sc.nextLine();
			if(choice.equals("1")){
				option1(size);
			}
			else if(choice.equals("2")){
				option2(size);
			}
			else if(choice.equals("3")){
				ok = false;
			}
			else{
				System.out.println("Please input a valid number");
			}
		}
		System.out.println("Thank you for using this program");	
	}
	
	private static void option1(int size){
		boolean ok = true;
		String msg = "";
		while(ok){
			System.out.print("Please input the message you want to encrypt: ");
			msg = sc.nextLine();
			if(msg.equals("")){
				System.out.println("Please input a message");
			}
			else{
				ok = false;
			}
		}
		
		String [] msgBinary = new String[msg.length()];
		char checkChar = 'a';
		String temp = "";
		//converting msg to binary
		for(int i = 0; i < msg.length(); i++){
			checkChar = msg.charAt(i);
			temp = Character.toString(checkChar);
			msgBinary[i] = asciiToBinary(temp);
			for(int j = msgBinary[i].length(); j < 8; j++){
				msgBinary[i] = "0" + msgBinary[i];
			}
		}
		
		int [][] cipher = new int[msg.length()][];
		int [][] decrypted = new int[msg.length()][];
		int sum = 0;
		int binSize = 0;
		int maxSize = 8;
		int sizeBin = 0;
		String [] chngToBin = new String[0];
		String [] ans = new String[0];
		if(size >= 8){
			sizeBin = 1;
			for(int i = 0; i < msg.length(); i++){
				cipher[i] = new int[1];
				decrypted[i] = new int[1];
				chngToBin = new String[1];
				ans = new String[1];
			}
		}
		else if(size >= 4){
			sizeBin = 2;
			for(int i = 0; i < msg.length(); i++){
				cipher[i] = new int[2];
				decrypted[i] = new int[2];
				chngToBin = new String[2];
				ans = new String[2];
			}
		}
		
		else if(size == 3){
			sizeBin = 3;
			for(int i = 0; i < msg.length(); i++){
				cipher[i] = new int[3];
				decrypted[i] = new int[3];
				chngToBin = new String[3];
				ans = new String[3];
			}
		}
		
		else if(size == 2){
			sizeBin = 4;
			for(int i = 0; i < msg.length(); i++){
				cipher[i] = new int[4];
				decrypted[i] = new int[4];
				chngToBin = new String[4];
				ans = new String[4];
			}
		}
		String decryptedBin = "";
		StringBuilder sb = new StringBuilder();
		String [] decryptedText = new String[msg.length()];
		
		//encrypting msgBinary with public key, knapsack
		for(int i = 0; i < msg.length(); i++){
			if(size < 8){
				if(size >= 4){
					if(size == 6){
						binSize = 0;
						for(int k = 0; k < 2; k++){
							sum = 0;
							for(int j = 0; j < size && binSize < maxSize; j++){
								checkChar = msgBinary[i].charAt(binSize);
								if(checkChar == '1'){
									sum += knapsack[j];
								}
								binSize++;
							}
							cipher[i][k] = sum;
						}
						//calculation of inverse * cipher % mod
						binSize = 0;
						for(int j = 0; j < 2; j++){
							decrypted[i][j] += (inverseValue * cipher[i][j]) % mod;	
						}
						//compare to superincreasing
						for(int k = 0; k < 2; k++){
							ans[k] = "";
							if(k == 0){
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i][k] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
								for(int j = 0; j < 2; j++){
									ans[k] += "0";
								}
							}
							else{
								for(int j = 1; j >= 0; j--){
									if(decrypted[i][k] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
								for(int j = 0; j < 6; j++){
									ans[k] = "0" + ans[k];
								}
							}
							//System.out.println(ans[k]);
						}
					}
					else if(size == 5){
						binSize = 0;
						for(int k = 0; k < 2; k++){
							sum = 0;
							for(int j = 0; j < size && binSize < maxSize; j++){
								checkChar = msgBinary[i].charAt(binSize);
								if(checkChar == '1'){
									sum += knapsack[j];
								}
								binSize++;
							}
							cipher[i][k] = sum;
						}
						//calculation of inverse * cipher % mod
						binSize = 0;
						for(int j = 0; j < 2; j++){
							decrypted[i][j] += (inverseValue * cipher[i][j]) % mod;	
						}
					
						//compare to superincreasing
						for(int k = 0; k < 2; k++){
							ans[k] = "";
							if(k == 0){
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i][k] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
								for(int j = 0; j < 3; j++){
									ans[k] += "0";
								}
							}
							else{
								for(int j = 2; j >= 0; j--){
									if(decrypted[i][k] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
								for(int j = 0; j < 5; j++){
									ans[k] = "0" + ans[k];
								}
							}
							//System.out.println(ans[k]);
						}
					}
					else if(size == 7){
						binSize = 0;
						for(int k = 0; k < 2; k++){
							sum = 0;
							for(int j = 0; j < size && binSize < maxSize; j++){
								checkChar = msgBinary[i].charAt(binSize);
								if(checkChar == '1'){
									sum += knapsack[j];
								}
								binSize++;
							}
							cipher[i][k] = sum;
						}
						//calculation of inverse * cipher % mod
						binSize = 0;
						for(int j = 0; j < 2; j++){
							decrypted[i][j] += (inverseValue * cipher[i][j]) % mod;	
						}
					
						//compare to superincreasing
						for(int k = 0; k < 2; k++){
							ans[k] = "";
							if(k == 0){
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i][k] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
								for(int j = 0; j < 1; j++){
									ans[k] += "0";
								}
							}
							else{
								for(int j = 0; j >= 0; j--){
									if(decrypted[i][k] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
								for(int j = 0; j < 7; j++){
									ans[k] = "0" + ans[k];
								}
							}
							//System.out.println(ans[k]);
						}
					}
					else if(size == 4){
						binSize = 0;
						for(int k = 0; k < 2; k++){
							sum = 0;
							for(int j = 0; j < size && binSize < maxSize; j++){
								checkChar = msgBinary[i].charAt(binSize);
								if(checkChar == '1'){
									sum += knapsack[j];
								}
								binSize++;
							}
							cipher[i][k] = sum;
						}
						//calculation of inverse * cipher % mod
						binSize = 0;
						for(int j = 0; j < 2; j++){
							decrypted[i][j] += (inverseValue * cipher[i][j]) % mod;	
						}
					
						//compare to superincreasing
						for(int k = 0; k < 2; k++){
							ans[k] = "";
							if(k == 0){
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i][k] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
								for(int j = 0; j < 4; j++){
									ans[k] += "0";
								}
							}
							else{
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i][k] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
								for(int j = 0; j < 4; j++){
									ans[k] = "0" + ans[k];
								}
							}
							//System.out.println(ans[k]);
						}
					}
					sb = new StringBuilder();
					for(int j = 0; j < 8; j++){
						sb.append(charOf(bitOf(ans[0].charAt(j)) ^ bitOf(ans[1].charAt(j))));
					}
					
					decryptedBin = sb.toString();
				}
				else if(size == 3){
					
					binSize = 0;
					for(int k = 0; k < 3; k++){
						sum = 0;
						for(int j = 0; j < size && binSize < maxSize; j++){
							checkChar = msgBinary[i].charAt(binSize);
							if(checkChar == '1'){
								sum += knapsack[j];
							}
							binSize++;
						}
						cipher[i][k] = sum;
					}
					//calculation of inverse * cipher % mod
					binSize = 0;
					for(int j = 0; j < 3; j++){
						decrypted[i][j] += (inverseValue * cipher[i][j]) % mod;	
					}
				
					//compare to superincreasing
					for(int k = 0; k < 3; k++){
						ans[k] = "";
						if(k == 0 || k == 1){
							for(int j = size-1; j >= 0; j--){
								if(decrypted[i][k] >= superIncreasing[j]){
									ans[k] = "1" + ans[k];
									decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
								}
								else{
									ans[k] = "0" + ans[k];
								}
							}
						}
						else{
							for(int j = 1; j >= 0; j--){
								if(decrypted[i][k] >= superIncreasing[j]){
									ans[k] = "1" + ans[k];
									decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
								}
								else{
									ans[k] = "0" + ans[k];
								}
							}
						}
						//System.out.println(ans[k]);
					}
					String DecryptedMsg = ans[0] + ans[1] + ans[2];
					sb = new StringBuilder();
					for(int j = 0; j < 8; j++){
						sb.append(charOf(bitOf(DecryptedMsg.charAt(j))));
					}
					
					decryptedBin = sb.toString();
				}
				else if(size == 2){
					binSize = 0;
					for(int k = 0; k < 4; k++){
						sum = 0;
						for(int j = 0; j < size && binSize < maxSize; j++){
							checkChar = msgBinary[i].charAt(binSize);
							if(checkChar == '1'){
								sum += knapsack[j];
							}
							binSize++;
						}
						cipher[i][k] = sum;
					}
					//calculation of inverse * cipher % mod
					binSize = 0;
					for(int j = 0; j < 4; j++){
						decrypted[i][j] += (inverseValue * cipher[i][j]) % mod;	
					}
				
					//compare to superincreasing
					for(int k = 0; k < 4; k++){
						ans[k] = "";
						
						for(int j = size-1; j >= 0; j--){
							if(decrypted[i][k] >= superIncreasing[j]){
								ans[k] = "1" + ans[k];
								decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
							}
							else{
								ans[k] = "0" + ans[k];
							}
						}
						
						//System.out.println(ans[k]);
					}
					String DecryptedMsg = ans[0] + ans[1] + ans[2] + ans[3];
					sb = new StringBuilder();
					for(int j = 0; j < 8; j++){
						sb.append(charOf(bitOf(DecryptedMsg.charAt(j))));
					}
					
					decryptedBin = sb.toString();
				}
			}
			else{
				sum = 0;
				for(int k = 0; k < 8; k++){
					checkChar = msgBinary[i].charAt(k);
					if(checkChar == '1'){
						sum += knapsack[k];
						//System.out.println(sum);
						//System.out.println("added: " + knapsack[k]);
					}
				}
				cipher[i][0] = sum;
				//calculation of inverse * cipher % mod
				binSize = 0;
				for(int j = 0; j < 1; j++){
					decrypted[i][j] += (inverseValue * cipher[i][j]) % mod;	
					//System.out.println(decrypted[i][j]);
				}
			
				//compare to superincreasing
				for(int k = 0; k < 1; k++){
					ans[k] = "";
					if(k == 0){
						for(int j = 7; j >= 0; j--){
							if(decrypted[i][k] >= superIncreasing[j]){
								ans[k] = "1" + ans[k];
								//System.out.println(decrypted[i][k]);
								decrypted[i][k] = decrypted[i][k] - superIncreasing[j];
								//System.out.println(decrypted[i][k]);
							}
							else{
								ans[k] = "0" + ans[k];
							}
						}
					}
					//System.out.println(ans[k]);
				}
				sb = new StringBuilder();
				for(int j = 0; j < 8; j++){
					sb.append(charOf(bitOf(ans[0].charAt(j))));
				}
				
				decryptedBin = sb.toString();
			}
		}
		
		System.out.println();
		System.out.print("Ciphertext:(");
		for(int i = 0; i < msg.length(); i++){
			for(int j = 0; j < sizeBin; j++){
				System.out.print(cipher[i][j]);
				if(j != sizeBin - 1){
					System.out.print(",");
				}
			}
			if(i != msg.length() - 1){
				System.out.print("),(");
			}
			else{
				System.out.print(")");
			}
		}
		System.out.println();
	}
	
	private static void option2(int size){
		int sum = 0;
		int binSize = 0;
		int maxSize = 8;
		int sizeBin = 0;
		
		if(size >= 8){
			sizeBin = 1;
		}
		else if(size >= 4){
			sizeBin = 2;
		}
		
		else if(size == 3){
			sizeBin = 3;
		}
		
		else if(size == 2){
			sizeBin = 4;
		}
		
		String [] chngToBin = new String[0];
		String [] ans = new String[0];
		
		boolean ok = true;
		int counter = 0;
		String ch = "";
		boolean check = true;
		int tempInt = 0;
		ArrayList<String> str = new ArrayList<String>();
		//get cipher text
		System.out.println("Please input the cipher text 1 by 1 (Enter q to stop)");
		while(ok){
			System.out.print(counter + ": ");
			ch = sc.nextLine();
			if(ch.equals("q")){
				if(((counter) % sizeBin) == 0){
					ok = false;
				}
				else{
					System.out.println("Please input more digits");
				}
			}
			else if(ch.length() != 0){
				check = checkDigit(ch);
				if(check){
					try{
						tempInt = Integer.parseInt(ch);
						throw new Exception("demo");
					}catch(Exception e){
		
					}
				
						str.add(ch);
						counter++;
				
				}
			}
			else{
				System.out.println("Please input some digits");
			}
		}
		if(str.size() != 0){
			int [] intEncrypted = new int[str.size()];
			for(int i = 0; i < str.size(); i++){
				try{
					intEncrypted[i] = Integer.parseInt(str.get(i));
					throw new Exception("demo");
				}catch(Exception e){
		
				}
			}
			String decryptedText = "";
			int [] decrypted = new int[str.size()];
			StringBuilder sb = new StringBuilder();
			char checkChar = 'a';
			if(size < 8){
				if(size >= 4){
					if(size == 6){
						for(int i = 0; i < str.size(); i+= 2){
							//calculation of inverse * cipher % mod
							binSize = 0;
						
							decrypted[i] = (inverseValue * intEncrypted[i]) % mod;	
							decrypted[i+1] = (inverseValue * intEncrypted[i+1]) % mod;
							ans = new String[2];
							//compare to superincreasing
							for(int k = 0; k < 2; k++){
								ans[k] = "";
								if(k == 0){
									for(int j = size-1; j >= 0; j--){
										if(decrypted[i] >= superIncreasing[j]){
											ans[k] = "1" + ans[k];
											decrypted[i] = decrypted[i] - superIncreasing[j];
										}
										else{
											ans[k] = "0" + ans[k];
										}
									}
									for(int j = 0; j < 2; j++){
										ans[k] += "0";
									}
								}
								else{
									for(int j = 1; j >= 0; j--){
										if(decrypted[i+1] >= superIncreasing[j]){
											ans[k] = "1" + ans[k];
											decrypted[i+1] = decrypted[i+1] - superIncreasing[j];
										}
										else{
											ans[k] = "0" + ans[k];
										}
									}
									for(int j = 0; j < 6; j++){
										ans[k] = "0" + ans[k];
									}
								}
							}
							sb = new StringBuilder();
							for(int j = 0; j < 8; j++){
								sb.append(charOf(bitOf(ans[0].charAt(j)) ^ bitOf(ans[1].charAt(j))));
							}
				
							String decryptedBin = sb.toString();
				
							int num = Integer.parseInt(decryptedBin,2);
							checkChar = (char)(int)num;
							decryptedText += Character.toString(checkChar);
						}
					}
					else if(size == 5){
						for(int i = 0; i < str.size(); i+= 2){
							//calculation of inverse * cipher % mod
							binSize = 0;
						
							decrypted[i] = ((inverseValue * intEncrypted[i]) % mod);	
							decrypted[i+1] = ((inverseValue * intEncrypted[i+1]) % mod);
							ans = new String[2];
							//compare to superincreasing
							for(int k = 0; k < 2; k++){
								ans[k] = "";
								if(k == 0){
									for(int j = size-1; j >= 0; j--){
										if(decrypted[i] >= superIncreasing[j]){
											ans[k] = "1" + ans[k];
											decrypted[i] = decrypted[i] - superIncreasing[j];
										}
										else{
											ans[k] = "0" + ans[k];
										}
									}
									for(int j = 0; j < 3; j++){
										ans[k] += "0";
									}
								}
								else{
									for(int j = 2; j >= 0; j--){
										if(decrypted[i+1] >= superIncreasing[j]){
											ans[k] = "1" + ans[k];
											decrypted[i+1] = decrypted[i+1] - superIncreasing[j];
										}
										else{
											ans[k] = "0" + ans[k];
										}
									}
									for(int j = 0; j < 5; j++){
										ans[k] = "0" + ans[k];
									}
								}
								//System.out.println(ans[k]);
							}
							sb = new StringBuilder();
							for(int j = 0; j < 8; j++){
								sb.append(charOf(bitOf(ans[0].charAt(j)) ^ bitOf(ans[1].charAt(j))));
							}
				
							String decryptedBin = sb.toString();
				
							int num = Integer.parseInt(decryptedBin,2);
							checkChar = (char)(int)num;
							decryptedText += Character.toString(checkChar);
						}
					}
					else if(size == 7){
						for(int i = 0; i < str.size(); i+= 2){
							//calculation of inverse * cipher % mod
							binSize = 0;
						
							decrypted[i] = ((inverseValue * intEncrypted[i]) % mod);	
							decrypted[i+1] = ((inverseValue * intEncrypted[i+1]) % mod);
							ans = new String[2];
							//compare to superincreasing
							for(int k = 0; k < 2; k++){
								ans[k] = "";
								if(k == 0){
									for(int j = size-1; j >= 0; j--){
										if(decrypted[i] >= superIncreasing[j]){
											ans[k] = "1" + ans[k];
											decrypted[i] = decrypted[i] - superIncreasing[j];
										}
										else{
											ans[k] = "0" + ans[k];
										}
									}
									for(int j = 0; j < 1; j++){
										ans[k] += "0";
									}
								}
								else{
									for(int j = 0; j >= 0; j--){
										if(decrypted[i+1] >= superIncreasing[j]){
											ans[k] = "1" + ans[k];
											decrypted[i+1] = decrypted[i+1] - superIncreasing[j];
										}
										else{
											ans[k] = "0" + ans[k];
										}
									}
									for(int j = 0; j < 7; j++){
										ans[k] = "0" + ans[k];
									}
								}
								//System.out.println(ans[k]);
							}
							sb = new StringBuilder();
							for(int j = 0; j < 8; j++){
								sb.append(charOf(bitOf(ans[0].charAt(j)) ^ bitOf(ans[1].charAt(j))));
							}
				
							String decryptedBin = sb.toString();
				
							int num = Integer.parseInt(decryptedBin,2);
							checkChar = (char)(int)num;
							decryptedText += Character.toString(checkChar);
						}
					}
					else if(size == 4){
						for(int i = 0; i < str.size(); i+= 2){
							//calculation of inverse * cipher % mod
							binSize = 0;
						
							decrypted[i] = ((inverseValue * intEncrypted[i]) % mod);	
							decrypted[i+1] = ((inverseValue * intEncrypted[i+1]) % mod);
							ans = new String[2];
							//compare to superincreasing
							for(int k = 0; k < 2; k++){
								ans[k] = "";
								if(k == 0){
									for(int j = size-1; j >= 0; j--){
										if(decrypted[i] >= superIncreasing[j]){
											ans[k] = "1" + ans[k];
											decrypted[i] = decrypted[i] - superIncreasing[j];
										}
										else{
											ans[k] = "0" + ans[k];
										}
									}
									for(int j = 0; j < 4; j++){
										ans[k] += "0";
									}
								}
								else{
									for(int j = size-1; j >= 0; j--){
										if(decrypted[i+1] >= superIncreasing[j]){
											ans[k] = "1" + ans[k];
											decrypted[i+1] = decrypted[i+1] - superIncreasing[j];
										}
										else{
											ans[k] = "0" + ans[k];
										}
									}
									for(int j = 0; j < 4; j++){
										ans[k] = "0" + ans[k];
									}
								}
								//System.out.println(ans[k]);
							}
							sb = new StringBuilder();
							for(int j = 0; j < 8; j++){
								sb.append(charOf(bitOf(ans[0].charAt(j)) ^ bitOf(ans[1].charAt(j))));
							}
				
							String decryptedBin = sb.toString();
				
							int num = Integer.parseInt(decryptedBin,2);
							checkChar = (char)(int)num;
							decryptedText += Character.toString(checkChar);
						}
					}
				
				}
				else if(size == 3){
					for(int i = 0; i < str.size(); i+= 3){
						//calculation of inverse * cipher % mod
						binSize = 0;
					
						decrypted[i] = ((inverseValue * intEncrypted[i]) % mod);	
						decrypted[i+1] = ((inverseValue * intEncrypted[i+1]) % mod);
						decrypted[i+2] = ((inverseValue * intEncrypted[i+2]) % mod);
					
						ans = new String[3];
						//compare to superincreasing
						for(int k = 0; k < 3; k++){
							ans[k] = "";
							if(k == 0){
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i] = decrypted[i] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
							}
							else if(k == 1){
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i+1] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i+1] = decrypted[i+1] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
							}
							else{
								for(int j = 1; j >= 0; j--){
									if(decrypted[i+2] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i+2] = decrypted[i+2] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
							}
						}
					
						String DecryptedMsg = ans[0] + ans[1] + ans[2];
						sb = new StringBuilder();
						for(int j = 0; j < 8; j++){
							sb.append(charOf(bitOf(DecryptedMsg.charAt(j))));
						}
				
						String decryptedBin = sb.toString();
						int num = Integer.parseInt(decryptedBin,2);
						checkChar = (char)(int)num;
						decryptedText += Character.toString(checkChar);
					
					}	
				
				}
				else if(size == 2){
					for(int i = 0; i < str.size(); i+= 4){
						//calculation of inverse * cipher % mod
						binSize = 0;
						ans = new String[4];
						decrypted[i] = ((inverseValue * intEncrypted[i]) % mod);	
						decrypted[i+1] = ((inverseValue * intEncrypted[i+1]) % mod);
						decrypted[i+2] = ((inverseValue * intEncrypted[i+2]) % mod);
						decrypted[i+3] = ((inverseValue * intEncrypted[i+3]) % mod);
			
						//compare to superincreasing
						for(int k = 0; k < 4; k++){
							ans[k] = "";
							if(k == 0){
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i] = decrypted[i] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
							}
							else if(k == 1){
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i+1] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i+1] = decrypted[i+1] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
							}
							else if(k == 2){
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i+2] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i+2] = decrypted[i+2] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
							}
							else if(k == 3){
								for(int j = size-1; j >= 0; j--){
									if(decrypted[i+3] >= superIncreasing[j]){
										ans[k] = "1" + ans[k];
										decrypted[i+3] = decrypted[i+3] - superIncreasing[j];
									}
									else{
										ans[k] = "0" + ans[k];
									}
								}
							}
							//System.out.println(ans[k]);
						}
						String DecryptedMsg = ans[0] + ans[1] + ans[2] + ans[3];
						//DecryptedMsg = DecryptedMsg.substring(2, 10);
						sb = new StringBuilder();
						for(int j = 0; j < 8; j++){
							sb.append(charOf(bitOf(DecryptedMsg.charAt(j))));
						}
				
						String decryptedBin = sb.toString();
				
						int num = Integer.parseInt(decryptedBin,2);
						checkChar = (char)(int)num;
						decryptedText += Character.toString(checkChar);
					}
				}
			}
			else{
				for(int i = 0; i < str.size(); i++){
					//calculation of inverse * cipher % mod
					binSize = 0;
				
					decrypted[i] = ((inverseValue * intEncrypted[i]) % mod);	
					ans = new String[1];
					//compare to superincreasing
					for(int k = 0; k < 1; k++){
						ans[k] = "";
						if(k == 0){
							for(int j = 7; j >= 0; j--){
								if(decrypted[i] >= superIncreasing[j]){
									ans[k] = "1" + ans[k];
									//System.out.println(decrypted[i][k]);
									decrypted[i] = decrypted[i] - superIncreasing[j];
									//System.out.println(decrypted[i][k]);
								}
								else{
									ans[k] = "0" + ans[k];
								}
							}
						}
						//System.out.println(ans[k]);
					}
					sb = new StringBuilder();
					for(int j = 0; j < 8; j++){
						sb.append(charOf(bitOf(ans[0].charAt(j))));
					}
			
					String decryptedBin = sb.toString();
			
					int num = Integer.parseInt(decryptedBin,2);
					checkChar = (char)(int)num;
					decryptedText += Character.toString(checkChar);
				}
			}
			/*
			int index = 0;
			while(index < decryptedBin.length()) {
				temp = decryptedBin.substring(index, index+8);
				int num = Integer.parseInt(temp,2);
				checkChar = (char)(int)num;
				decryptedText[i] = "";
				decryptedText[i] += Character.toString(checkChar);
				index +=9;
			}
			*/
			System.out.println("The decrypted message: " + decryptedText);
		}
	}
	
	private static boolean bitOf(char in) {
		return (in == '1');
	}

	private static char charOf(boolean in) {
	    	return (in) ? '1' : '0';
	}
	
	public static String asciiToBinary(String asciiString){  
		byte[] bytes = asciiString.getBytes();  
		StringBuilder binary = new StringBuilder();  
		for (byte b : bytes)  {  
			int val = b;  
			for (int i = 0; i < 8; i++)  {  
				binary.append((val & 128) == 0 ? 0 : 1);  
				val <<= 1;  
			}  
		}  
		return binary.toString();  
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
	
	public static boolean checkBigger(int [] superIncreasing, int tempInt, int size){
		int total = 0;
		for(int i = 0; i < size; i++){
			total += superIncreasing[i];
		}
		if(total >= tempInt){
			return false;
		}
		return true;
	}
	
	public static boolean checkInverse(int n1, int n2){
		int temp = 0;
		if(n2 > n1){
			temp = n2;
			n2 = n1;
			n1 = temp;
			
		}

		int q = 0;
		int r = 0;
		int a1 = 1;
		int a2 = 0;
		int b1 = 0;
		int b2 = 1;
		int tempA = 0;
		int tempB = 0;
		temp = n1;
		int zero = 0;
		
		boolean ok = true;
		boolean check = true;
		
		//2nd and so on
		while(ok){
			q = n1 / n2;
			r = n1 % n2;
			n1 = n2;
			n2 = r;
			tempA = a2;
			tempB = b2;
			a2 = a1 - (a2 * q);
			b2 = b1 - (b2 * q);
			a1 = tempA;
			b1 = tempB;
			
			if(r == 1){
				check = true;
				ok = false;
			}
			else if(r == 0){
				check = false;
				ok = false;
			}
		}
		return check;
	}
	public static boolean checkPrime(int n){  
		int m = 0;
		int flag = 0;      
		m = n/2;      
		if(n == 0|| n == 1){  
			return false;  
		}
		else{  
			for(int i = 2; i <= m; i++){      
				if(n%i==0){      
					return false;        
				}      
			}      
			if(flag==0){ 
				return true;
			}  
		} 
		return false;
	}
	
	public static int getInverse(int n1, int n2){
		int temp = 0;
		if(n2 > n1){
			temp = n2;
			n2 = n1;
			n1 = temp;
			
		}

		int q = 0;
		int r = 0;
		int a1 = 1;
		int a2 = 0;
		int b1 = 0;
		int b2 = 1;
		int tempA = 0;
		int tempB = 0;
		temp = n1;
		int zero = 0;
		
		boolean ok = true;

		//2nd and so on
		while(ok){
			q = n1 / n2;
			r = n1 % n2;
			n1 = n2;
			n2 = r;
			tempA = a2;
			tempB = b2;
			a2 = a1 - (a2 * q);
			b2 = b1 - (b2 * (q));
			a1 = tempA;
			b1 = tempB;
			
			if(r == 0){
				ok = false;
			}
		}
		
		if(b1 < 0){
			b1 = b1 + temp;
		}
		
		return b1;
	}
}
