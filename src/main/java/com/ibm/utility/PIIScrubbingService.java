package com.ibm.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PIIScrubbingService {
	
	private static String SCRUB_SETTING="scrub";

	public PIIScrubbingService() {
	}
	
	/**
	* Applies the specified mask to the card number.
	*
	* @param cardNumber The card number in plain format
	* @param mask The number mask pattern. Use # to include a digit from the
	* card number at that position, use x to skip the digit at that position
	*
	* @return The masked card number
	*/
	public static String scrubPIIData(String input, String scrubOrStrip, String scrubKey) {
		
		Matcher match = Pattern.compile("([0-9]){2,}", Pattern.CASE_INSENSITIVE).matcher(input);
		String tempInput = "";
		/*if(match.find()){
			System.out.println("Yes");
			System.out.println(match.group());
			match.group()
			input = input.replaceAll("([0-9])", key);
		}else{
			System.out.println("No");
		}*/
		while(match.find()){
			//System.out.println(match.group().replaceAll("([0-9])", key));
			int index = match.start();
			//input = input.substring(index) + input.replaceAll("([0-9])", key);
			if (SCRUB_SETTING.equalsIgnoreCase(scrubOrStrip)) {
				// configuration is to scrub the data
				tempInput = input.substring(0, index)+match.group().replaceAll("([0-9])", scrubKey);				
			} else {
				// configuration is to strip the data
				tempInput = input.substring(0, index)+match.group().replaceAll("([0-9])", "");
			}
			//match = Pattern.compile("([0-9]){2,}", Pattern.CASE_INSENSITIVE).matcher(input);
			System.out.println(tempInput);
			input = tempInput + input.substring(tempInput.length());
			System.out.println(index);
			System.out.println(input);
			
		}
		return input;
		
	}
	
	public static void main(String[] args){
		
		String scrubOrStrip="scrub";
		String scrubKey="X";
		
		String str = "WEWEW24-12ERE3123-23ERE232";
		String str1 = "JWUEEWWE1YTYTYTYT66UYUYUYU8888yuyuuu8766755";
		String str2 = "A1B1C9D3GHHDH&&&&*&5HGHGh*JHJHJ8";
		String str3 = "A1B23C34D67893";
		
		String maskedNumber = scrubPIIData(str, scrubOrStrip, scrubKey);
		String maskedNumber1 = scrubPIIData(str1, scrubOrStrip, scrubKey);
		String maskedNumber2 = scrubPIIData(str2, scrubOrStrip, scrubKey);
		String maskedNumber3 = scrubPIIData(str3, scrubOrStrip, scrubKey);
		
		System.out.println("Input after scrubbing : " + maskedNumber);
		System.out.println("Input after scrubbing : " + maskedNumber1);
		System.out.println("Input after scrubbing : " + maskedNumber2);
		System.out.println("Input after scrubbing : " + maskedNumber3);
	}
}
