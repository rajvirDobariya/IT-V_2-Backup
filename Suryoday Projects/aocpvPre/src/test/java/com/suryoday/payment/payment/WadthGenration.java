package com.suryoday.payment.payment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class WadthGenration {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
       String wadh="";
		wadh = wadh + "2.5PYNYY";
		System.out.println("v1    "+wadh);
		
			System.out.println("v1    "+getWadhHash(wadh));
		
	}
	
	public static String getWadhHash(String wadh) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		digest.reset();
		//return digest.digest(wadh.getBytes());
		
		System.out.println("getWadhHash :" + new String(wadh.getBytes()));
		return new String(Base64.encodeBase64(getHash(wadh))).trim();
	}
	
	public static byte[] getHash(String wadh) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		digest.reset();
		return digest.digest(wadh.getBytes());
	}

}
