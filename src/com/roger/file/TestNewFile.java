package com.roger.file;

import java.io.File;
import java.io.IOException;

public class TestNewFile {
	
	public static void main(String[] args) throws IOException {
		File file = new File("resources/jdbc.properties");
		System.out.println(file.getCanonicalPath());
		System.out.println(file.exists());
	}
}
