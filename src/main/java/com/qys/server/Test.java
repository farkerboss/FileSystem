package com.qys.server;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.qys.util.AesUtil;
import com.qys.util.RsaUtil;

public class Test {
	@org.junit.Test
	public void AesTest() {
		// 16位
		String key = "MIGfMA0GCSqGSIb3";
		// 字符串
		String str = "huanzi.qch@qq.com:欢子";
		try {
			// 加密
			String encrypt = AesUtil.encrypt(str, key);
			// 解密
			String decrypt = AesUtil.decrypt(encrypt, key);
			System.out.println("加密前：" + str);
			System.out.println("加密后：" + encrypt);
			System.out.println("解密后：" + decrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@org.junit.Test
	public void RsaTest() throws Exception {

		String publicKey = RsaUtil.getPublicKey();
		String privateKey = RsaUtil.getPrivateKey();
		String str = "123";
		String encrypt = bytesToString(RsaUtil.encryptByPrivateKey(str.getBytes(), privateKey)); // 解密
		byte[] decrypt = RsaUtil.decryptByPublicKey(stringToBytes(encrypt), publicKey);
		String s = new String(decrypt);
		System.out.println("加密前：" + str);
		System.out.println("加密后：" + encrypt);
		System.out.println("解密后：" + s);

	}

	@org.junit.Test
	public void urlEncodeTest() {
		String str = "123来康康";
		String s1 = URLEncoder.encode(str);
		String s2 = URLDecoder.decode(s1);
		System.out.println("编码前：" + str);
		System.out.println("编码后：" + s1);
		System.out.println("解编码后：" + s2);
	}

	protected static byte[] stringToBytes(String data) {
		String[] strArr = data.split(" ");
		int len = strArr.length;
		byte[] clone = new byte[len];
		for (int i = 0; i < len; i++) {
			clone[i] = Byte.parseByte(strArr[i]);
		}

		return clone;
	}

	protected static String bytesToString(byte[] encrytpByte) {
		String result = "";
		for (Byte bytes : encrytpByte) {
			result += bytes.toString() + " ";
		}
		return result;
	}

}
