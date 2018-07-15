package com.roger.file;

import java.io.File;
import java.io.FileFilter;

public class TestFilterFile {
	
	public static void main(String[] args) {
		//找到C:/Windows盘目录下的所有exe文件
		File file = new File("C:/Windows");
		
		File[] exeFiles = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File varF) {
				return varF.getName().endsWith(".exe");
			}
		});
		for (File varFile : exeFiles) {
			System.out.println(varFile);
		}
	}
}
