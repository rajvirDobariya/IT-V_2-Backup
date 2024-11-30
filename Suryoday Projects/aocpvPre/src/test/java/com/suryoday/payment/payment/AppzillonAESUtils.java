package com.suryoday.payment.payment;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppzillonAESUtils {

    private static final String SHA_1 = "SHA-1";
	private static final String UTF_8 = "UTF-8";
    private static final String AES = "AES";

    private static final  String PBS_PADDING = "AES/CBC/PKCS5padding";
	
	 private static Logger LOG = LoggerFactory.getLogger(AppzillonAESUtils.class);

    public static String encryptString(String pkey, String poriginalstring) {
    	LOG.debug("encryting String ");
        String encyptedstring = "";
        try {
            byte[] key = (pkey).getBytes(UTF_8);

            MessageDigest sha = MessageDigest.getInstance(SHA_1);
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            LOG.debug("Key for encrytion:" + new String(key));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encrypted = cipher.doFinal((poriginalstring).getBytes());

            encyptedstring = Base64.encodeBase64String(encrypted);
            LOG.debug( "length encrypted string: "
                    + encyptedstring.length());

        } catch (UnsupportedEncodingException e) {
            LOG.error("UnsupportedEncodingException", e);
        } catch (InvalidKeyException e) {
        	LOG.error( "InvalidKeyException", e);
        } catch (NoSuchAlgorithmException e) {
        	LOG.error( "NoSuchAlgorithmException", e);
        } catch (BadPaddingException e) {
        	LOG.error("BadPaddingException", e);
        } catch (IllegalBlockSizeException e) {
        	LOG.error("IllegalBlockSizeException", e);
        } catch (NoSuchPaddingException e) {
        	LOG.error("NoSuchPaddingException", e);
        }
        return encyptedstring;
    }

    public static String decryptString(String pkey, String pencrypted) {
    	LOG.debug("Decrypting String ");
        String originalString = "";
        try {
            byte[] key = (pkey).getBytes(UTF_8);

            MessageDigest sha = MessageDigest.getInstance(SHA_1);
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            LOG.debug("Key for encrytion:" + new String(key));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] original = cipher.doFinal(Base64.decodeBase64(pencrypted));
            originalString = new String(original);
            LOG.debug("originalString: " + originalString);

        } catch (UnsupportedEncodingException e) {
            LOG.error( "UnsupportedEncodingException",e);
        } catch (InvalidKeyException e) {
        	LOG.error( "InvalidKeyException",e);
        } catch (NoSuchAlgorithmException e) {
        	LOG.error( "NoSuchAlgorithmException",e);
        } catch (BadPaddingException e) {
        	LOG.error( "BadPaddingException",e);
        } catch (IllegalBlockSizeException e) {
        	LOG.error("IllegalBlockSizeException",e);
        } catch (NoSuchPaddingException e) {
        	LOG.error( "NoSuchPaddingException",e);
        }
        return originalString;

    }

    
     public static String getExpirableKey(boolean eXPInMin,boolean eXPInHr,boolean eXPInDay) {

        Timestamp t = new Timestamp(new Date().getTime());

        Calendar lCalendar = Calendar.getInstance();
        lCalendar.setTimeInMillis(t.getTime());
        if( eXPInMin){
        lCalendar.set(Calendar.SECOND, 0);
        lCalendar.set(Calendar.MILLISECOND, 0);
        }else if ( eXPInHr){
        	 lCalendar.set(Calendar.MINUTE, 0);
             lCalendar.set(Calendar.SECOND, 0);
             lCalendar.set(Calendar.MILLISECOND, 0);
        	
        }else if ( eXPInDay){
        	 lCalendar.set(Calendar.HOUR_OF_DAY, 0);
             lCalendar.set(Calendar.MINUTE, 0);
             lCalendar.set(Calendar.SECOND, 0);
             lCalendar.set(Calendar.MILLISECOND, 0);
        }
        Timestamp date = new Timestamp(lCalendar.getTimeInMillis());
        return  date.toString();
    }
     
  // key generator method
 	public static byte[] hmacSha1(String salt, String key) {
 		SecretKeyFactory factory = null;
 		Key keyByte = null;
 		try {
 			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
 			KeySpec keyspec = new PBEKeySpec(key.toCharArray(),
 					salt.getBytes("UTF-8"), 2, 128);
 			keyByte = factory.generateSecret(keyspec);
 		} catch (NoSuchAlgorithmException e) {
 			//e.printStackTrace();
 		} catch (InvalidKeySpecException e) {
 			//e.printStackTrace();
 		}
 		catch (UnsupportedEncodingException e) {
 			LOG.error("Exception",e);
 		}
 		return keyByte.getEncoded();
 	}

 	// encryption method
 	public  static String encryptString(String cypher, String key,String clearText, String salt,byte[] iv) {
 		SecretKeySpec skeySpec = null;
 		skeySpec = new SecretKeySpec(hmacSha1(salt, key), "AES");
 		try {
 			Cipher cipher = Cipher.getInstance(cypher);
 			//iv = ivText.getBytes();
 			// random.nextBytes(iv);
 			IvParameterSpec ivParams = new IvParameterSpec(iv);
 			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParams);
 			byte[] encryptedData = cipher.doFinal(clearText.getBytes("UTF-8"));
 			if (encryptedData == null)
 				return null;
 			return Base64.encodeBase64String(encryptedData);
 		} catch (Exception e) {
 			LOG.error( "Exception",e);
 		}
 		return null;
 		/**/
 	}

 	// decryption method
 	public  static String decryptString(String cypher, String key,
 			String textToDecrypt, String salt,byte[] iv) {
 		LOG.debug("Cipher -:" + cypher + ", key:"+ key + ", texToDecrypt :" + textToDecrypt + ", salt :" + salt + ", iv:" + iv + ", OS -:");
 		SecretKeySpec skeySpec = null;
 		
 		skeySpec = new SecretKeySpec(hmacSha1(salt, key), "AES");
 		
 		try {
 			Cipher cipher = Cipher.getInstance(cypher);
 			IvParameterSpec ivParams = new IvParameterSpec(iv);
 			cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParams);
 			byte[] plaintext = cipher.doFinal(Base64.decodeBase64(textToDecrypt));
 			String plainrStr = new String(plaintext, "UTF-8");
 			return new String(plainrStr);
 		} catch (Exception e) {
 			LOG.error("Exception",e);
 		}
 		return null;
 	}
 	/**
	 * prepares the IV from key
	 * @param key
	 * @return
	 */
	public static byte[] getIV(String key) {
		byte[]iv = new byte[16];
		java.util.Arrays.fill(iv, (byte)0);
		StringBuffer or = new StringBuffer(key);
		String nw = or.reverse().toString();
		byte[]keyBytes = null;
		try {
			keyBytes = nw.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		}
		byte[]rawIV = new byte[keyBytes.length];
		for (int i = 0; i < keyBytes.length; i++) {
			rawIV[i] = (byte)(keyBytes[i] >> 1);
		}
		for (int i = 0; i < iv.length; i++) {
			iv[i] = rawIV[i];
		}
		return iv;
	}
	
	/**
	 * Prepares the salt based on key
	 *
	 * @param key
	 * @return
	 */
	public static String getSalt(String key) {
		// TODO Auto-generated method stub
		String originalString = key;

		char[]c = originalString.toCharArray();

		// Replace with a "swap" function, if desired:
		char temp = c[0];
		c[0] = c[1];
		c[1] = temp;

		temp = c[c.length - 1];
		c[c.length - 1] = c[c.length - 2];
		c[c.length - 2] = temp;
		String swappedString = new String(c);
		return swappedString;
	}
	
	public static String decryptContainerString(String pCipherString, String pKey){
		String decryptedString = null;
		String lKey = getPaddedKey(pKey);
		LOG.debug(" decryptContainerString key for encryption after checking the length -:"
				+ lKey);
		byte[] iv = AppzillonAESUtils.getIV(lKey);
		String finalSalt = AppzillonAESUtils.getSalt(lKey);
		decryptedString = AppzillonAESUtils.decryptString(PBS_PADDING, lKey, pCipherString,
						finalSalt, iv);
//		LOG.debug(ServerConstants.LOGGER_PREFIX_RESTFULL
//				+ " decryptContainerString Decrypted AppzillonBody -:"+ decryptedString);
		return decryptedString;
	}

	private static String getPaddedKey(String pKey) {
		String lKey = pKey;
		String paddingMask = "$$$$$$$$$$$$$$$$";
		if (lKey.length() <= 16) {
			lKey += paddingMask.substring(0, 16 - lKey.length());
		}
		if (lKey.length() > 16) {
			lKey = lKey.substring(0, 16);
		}
		return lKey;
	}
	public static String encryptStringtoContainer(String pPlainText, String pKey){
		String encryptedString = null;
		String lKey = getPaddedKey(pKey);
		LOG.debug(" encryptStringtoContainer key for decryption after checking the length -:"
				+ lKey);
		
		byte[] iv = AppzillonAESUtils.getIV(lKey);
		String finalSalt = AppzillonAESUtils.getSalt(lKey);
		
		encryptedString = AppzillonAESUtils.encryptString(PBS_PADDING,
				lKey, pPlainText, finalSalt, iv);
		LOG.debug("encryptStringtoContainer Encrypted Body -:" + encryptedString);
		return encryptedString;
		
	}
	
	public static void main(String args[]){
		
		String key = "75ae66f7-8504-4101-97da-e3f07542cbb4";
		

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, dd MM yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String todayDate = dtf.format(now);
		LOG.debug("got todayDate " + todayDate);
		if (key.isEmpty()) {
			key = todayDate;
		}

		LOG.debug("Create session Id");

	
		String sessionId = getRandomString();
		LOG.debug("got session Id" + sessionId);

		LOG.debug("Create Auth Key");



		String requestId = getRandomSessionKey();
		LOG.debug("got requestId " + requestId);

		
		
		String encryptedText = "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";
		// key = "12345com.iexceed.qateam" ;
		String paddingMask = "$$$$$$$$$$$$$$$$";
		String planReq = "{\"ChannelId2\":\"BALANCEINQ_REQ\"}";
		System.out.println("key.length() -:" + key.length());
		if (key.length() <= 16) {
			key += paddingMask.substring(0, 16 - key.length());
		}
		if(key.length()>16){
			key = key.substring(0,16);
		}
		System.out.println("key -:" +  key);
		byte[]iv = AppzillonAESUtils.getIV(key);
		String finalSalt = AppzillonAESUtils.getSalt(key);
		String PBS_PADDING = "AES/CBC/PKCS5padding";
		String encryptS = AppzillonAESUtils.encryptString(PBS_PADDING, key, planReq, finalSalt, iv);
		System.out.println("encryptS -:" + encryptS);
		String decryptedString = AppzillonAESUtils.decryptString(PBS_PADDING, key, encryptedText, finalSalt, iv);
		System.out.println("decryptedString -:" + decryptedString);
	}	
	
	
	private static String getRandomString() {
		LOG.debug("getRandomString");

		byte[] randomByte = getRandomByte();
		return Base64.encodeBase64String(randomByte);
	}

	private static String getRandomSessionKey() {
		LOG.debug("getRandomSessionKey");

		SecureRandom lSecRand = new SecureRandom();
		Double lRand = lSecRand.nextDouble();

		return "" + lRand;

	}

	private static byte[] getRandomByte() {
		LOG.debug("getRandomByte");

		byte[] randomByte = new byte[24];
		SecureRandom secRand = new SecureRandom();
		randomByte = new byte[24];
		secRand.nextBytes(randomByte);
		return randomByte;
	}
	


}