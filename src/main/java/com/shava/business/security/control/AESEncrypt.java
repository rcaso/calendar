package com.shava.business.security.control;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncrypt {
	
	private static final String SEED="AesSEcREtkeyABCD";
	
	private static Key generateKeyFromString(final String secKey) throws Exception {
	    final byte[] keyVal = secKey.getBytes("UTF-8");
	    final Key key = new SecretKeySpec(keyVal, "AES");
	    return key;
	}
	
	public static String encrypt(String plainText)
			throws Exception {
		Key secretKey = generateKeyFromString(SEED);
		Cipher cipher = Cipher.getInstance("AES");
		byte[] plainTextByte = plainText.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		Base64.Encoder encoder = Base64.getEncoder();
		String encryptedText = encoder.encodeToString(encryptedByte);
		return encryptedText;
	}

	public static String decrypt(String encryptedText)
			throws Exception {
		Key secretKey = generateKeyFromString(SEED);
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] encryptedTextByte = decoder.decode(encryptedText);
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}
	
	public static void main(String[] args) throws Exception {
		String plainText = "1234";
		System.out.println("Plain Text Before Encryption: " + plainText);

		String encryptedText = encrypt(plainText);
		System.out.println("Encrypted Text After Encryption: " + encryptedText);

		String decryptedText = decrypt(encryptedText);
		System.out.println("Decrypted Text After Decryption: " + decryptedText);
	}
	
	

}
