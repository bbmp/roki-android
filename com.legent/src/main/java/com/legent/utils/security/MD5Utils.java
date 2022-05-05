package com.legent.utils.security;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

public class MD5Utils {

	public static String Md5(String str) {

		HashCode hashCode= Hashing.md5().hashString(str, Charsets.UTF_8);
		String md5=hashCode.toString();
		return md5;
	}

}
