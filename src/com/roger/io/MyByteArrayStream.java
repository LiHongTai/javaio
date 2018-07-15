package com.roger.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MyByteArrayStream {

	public static void main(String[] args) throws Exception {
		String word = "abcdef";

		byte[] bytes = word.getBytes();

		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		int x = -1;
		while ((x = inputStream.read()) != -1) {
			System.out.println("x=" + x);
		}
		inputStream.close();

		// 在构造字节数组输出流时会同时在内部构造一个512个字节的字节数组
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
		outputStream.write("今天天气真好".getBytes("utf8"));
		outputStream.close();
		//result 就是字节数组输出流的目的地
		byte[] result = outputStream.toByteArray();

		String rword = new String(result, "utf8");
		System.out.println("rword=" + rword);
	}
}
