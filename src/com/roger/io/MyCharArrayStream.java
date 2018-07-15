package com.roger.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.channels.ServerSocketChannel;

public class MyCharArrayStream {

	public static void main(String[] args) throws Exception {

		String word = "今天天气真好";
		// 使用输出字节流写
		// FileOutputStream out = new FileOutputStream("resources/arrayStream.txt");
		// out.write(word.getBytes("utf8"));// 如果不传递utf8参数，则采用默认编码方式
		// out.close();

		// 使用输入字节流读
		// FileInputStream in = new FileInputStream("resources/arrayStream.txt");
		// byte[] result = new byte[512];
		// int len = in.read(result, 0, 512);
		// in.close();
		// String rword = new String(result, 0, len, "utf8");
		// System.out.println("rword:" + rword);

		// 采用输出字节流写
		FileWriter writer = new FileWriter("resources/arrayStream.txt");
		writer.write(word);// 采用默认编码方式
		writer.close();

		// 采用字符输入流读
		FileReader reader = new FileReader("resources/arrayStream.txt");
		char[] cbuf = new char[512];
		reader.read(cbuf, 0, 512);
		reader.close();
		String rword = new String(cbuf);
		System.out.println("rword:" + rword);
	}
}
