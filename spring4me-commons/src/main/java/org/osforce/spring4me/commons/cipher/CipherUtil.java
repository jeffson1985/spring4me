package org.osforce.spring4me.commons.cipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 30, 2011 - 4:11:57 PM <a
 *         href="http://www.opensourceforce.org">开源力量</a>
 */
@SuppressWarnings("restriction")
public class CipherUtil {

	private static final String ALGORITHM = "DES";
	
	public static String encrypt(String data, String file) throws Exception {
		return new BASE64Encoder().encode(encrypt(data.getBytes(), file));
	}

	public static byte[] encrypt(byte[] data, String file) throws Exception {
		Key key = getKey(file);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(data);
	}
	
	public static String decrypt(String data, String file) throws Exception {
		return new String(decrypt(new BASE64Decoder().decodeBuffer(data), file));
	}

	public static byte[] decrypt(byte[] data, String file) throws Exception {
		Key key = getKey(file);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	public static Key getKey(String file) throws Exception {
		Key key = null;
		File keyFile = new File(file);
		if (keyFile.exists()) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(keyFile));
			key = (Key) ois.readObject();
			ois.close();
		} else {
			KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
			keyGen.init(56);
			key = keyGen.generateKey();
			keyFile.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyFile));
			oos.writeObject(key);
			oos.close();
		}
		return key;
	}

}
