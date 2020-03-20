package com.yss.util;

public class StringConversion {
	public static byte[] stringToBytes(String data) {
	    String[] strArr = data.split(" ");
	    int len = strArr.length;
	    byte[] clone = new byte[len];
	    for (int i = 0; i < len; i++) {
	        clone[i] = Byte.parseByte(strArr[i]);
	    }

	    return clone;
	}
	public static String bytesToString(byte[] encrytpByte) {
	    String result = "";
	    for (Byte bytes : encrytpByte) {
	        result += bytes.toString() + " ";
	    }
	    return result;
	}
}
