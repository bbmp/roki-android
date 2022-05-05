package com.legent.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.graphics.Bitmap;

public class StreamUtils {

	final static String ENCODING = "UTF-8";

	/**
	 * 将InputStream转换成String
	 * @param inputStream
	 * @return
	 */
	public static String stream2String(InputStream inputStream) {
		InputStreamReader in = null;
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		String line;
		try {
			in = new InputStreamReader(inputStream, ENCODING);
			reader = new BufferedReader(in);

			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}

			reader.close();
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader = null;
			in = null;
		}

		return sb.toString();
	}

	/**
	 * 将byte[]转换成InputStream
	 * @param b
	 * @return
	 */
	public static InputStream bytes2Stream(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		return bais;
	}

	/**
	 * 将InputStream转换成byte[]
	 * @param in
	 * @return
	 */
	public static byte[] stream2Bytes(InputStream in) {
		String str = "";
		byte[] readByte = new byte[1024];
		try {
			while (in.read(readByte, 0, 1024) != -1) {
				str += new String(readByte).trim();
			}
			return str.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将Bitmap转换成InputStream
	 * @param bm
	 * @return
	 */
	public static InputStream bitmap2stream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * 将Bitmap转换成InputStream
	 * @param bmp
	 * @param format
	 * @param quality
	 * @return
	 */
	public static InputStream bitmap2stream(Bitmap bmp, Bitmap.CompressFormat format,
			int quality) {
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			bmp.compress(format, quality, baos);
			is = new ByteArrayInputStream(baos.toByteArray());
			baos.close();
		} catch (Exception e) {
		} finally {
			baos = null;
		}

		return is;
	}

}
