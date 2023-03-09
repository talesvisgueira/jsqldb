package br.com.mtinet.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public abstract class FileLog {
	protected String filePath;
	
	protected boolean printLog = false;
	
	public void createNewFile(File file) throws Exception{
		if (file.exists()) file.delete();
		try {
			file.createNewFile();
		} catch(Exception e) { throw new Exception("Nao foi poss�vel criar o arquivo " + this.filePath);}
	}
	
	public void createNewFile() throws Exception{
		File file = new File(filePath);
		createNewFile(file);
		
	}
	
	public void save(String msg) {
		if (this.printLog) System.out.println(msg);
		try{ 
			FileWriter fstream = new FileWriter(this.filePath,true);
			writeFile(fstream,msg);
		}catch (Exception e){
			 System.err.println("Error: " + e.getMessage());
		}
	}
	
	private void writeFile(FileWriter fstream,String msg) throws Exception {
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(msg+"\r\n");
		out.close();
	}
	
	public void save(File file,String msg) {
		if (this.printLog) System.out.println(msg);
		try{ 
			FileWriter fstream = new FileWriter(file,true);
			writeFile(fstream,msg);
		}catch (Exception e){
			 System.err.println("Error: " + e.getMessage());
		}
	}
	
}
