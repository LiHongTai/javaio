package com.roger.file;

import java.io.File;

public class TestTraversalFile {
	
	public static void main(String[] args) {
		File file = new File("C:/Windows");
		String[] fileDir = file.list();
		for (String fileDirStr : fileDir) {
			System.out.println(fileDirStr);
		}
		
		File[] files = file.listFiles();
		for (File tempFile : files) {
			System.out.println(tempFile);
		}
				
	}
}
