package net.seabears.signature;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader {
	
	public FileReader() {
	}
	
	/**
	 * reads a file and returns the file data as a string
	 * 
	 * @param fileName represents the file name (ex: file.txt)
	 * @return returns the file data as a string
	 */
	public String readFile(String fileName) {
	  	String fileData = "";
	  	try(BufferedReader br = new BufferedReader(new java.io.FileReader(fileName))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
	
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    fileData = sb.toString();
	  	} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	  	
	  	return fileData;
	  }

}
