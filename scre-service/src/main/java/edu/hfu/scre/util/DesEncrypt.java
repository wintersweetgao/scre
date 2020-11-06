package edu.hfu.scre.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * 3DES加密解密工具
 * 
 * @author ping
 *
 */
public class DesEncrypt {
	private static Key key;
	private static final String KEY_ALGORITHM = "DESede";
	private static final String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";
	private final static Base64.Decoder decoder = Base64.getDecoder();
	private final static Base64.Encoder encoder = Base64.getEncoder();

	static {
		toKey("我的中国好大的家");
	}

	/**
	 * 初始化Key对象
	 * 
	 * @param keyText
	 * @return
	 */
	private static void toKey(String keyText) {
		DESedeKeySpec dks;
		SecretKeyFactory factory = null;
		byte[] keys=check(keyText);
		if (null!=keys) {
			try {
				dks = new DESedeKeySpec(keys);
				factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
				key = factory.generateSecret(dks);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			throw new java.lang.RuntimeException("密钥长度小于24");
		}
	}

	/**
	 *
	 * @param 密钥keyText
	 * @return 校验密钥格式是否通过
	 */
	private static byte[] check(String keyText) {
		if (keyText != null && !"".equals(keyText.trim())) {
			try {
				byte[] keyBytes=keyText.getBytes("utf-8");
				if (keyBytes.length>= 24) {
					return keyBytes;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param 明文 inputText
	 * @return 密文 outputText
	 */
	public static String getEncString(String inputText) {
		Cipher cipher = null;
		String outputText = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			outputText = encoder.encodeToString(cipher.doFinal(inputText.getBytes("UTF8")));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return outputText;
	}

	/**
	 * 解密
	 * 
	 * @param 密文 inputText
	 * @return 明文 outputText
	 */
	public static  String getDesString(String inputText) {
		Cipher cipher = null;
		String outputText = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			outputText = new String(cipher.doFinal(decoder.decode(inputText)));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return outputText;
	}
	
	public static void main(String[] args) {
		String key="我的中国好大的家";
		String ming="中国";
		String mi=DesEncrypt.getEncString(ming);
		System.out.println(mi);
		System.out.println(DesEncrypt.getDesString(mi));
	}

}
