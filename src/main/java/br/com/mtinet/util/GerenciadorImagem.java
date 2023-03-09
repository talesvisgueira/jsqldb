package br.com.mtinet.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GerenciadorImagem {
	
	public static byte[] imageToBytes(Image image) throws IOException{    
		BufferedImage buffer = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);		
		WritableRaster raster = buffer.getRaster();
	    DataBufferByte dataBuffer = (DataBufferByte)raster.getDataBuffer();
	    return dataBuffer.getData();
	}   
	
	public static Image bytesToImage(byte[] bytesImage) {
		Image image = null;
		try{   			
			ByteArrayInputStream array = new ByteArrayInputStream(bytesImage);      
			image = ImageIO.read(array);
		}   
		catch(Exception e){ }  
		return image;
	}

	public static byte[] carregarImagem(String caminhoImagem, int tamanhoMaximo) throws Exception{
		byte[] image = carregarImagem(caminhoImagem);
		if (image.length > tamanhoMaximo * 1024 ) throw new Exception("Excedeu o tamanho m�ximo do arquivo para armazenamento.");
		return image;
	}
	
	public static byte[] carregarImagem(String caminhoImagem){
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = 0;
		FileInputStream fi = null;
		try{
		   fi = new FileInputStream(caminhoImagem);
		   while ((bytesRead = fi.read(buffer))!=-1){
		   		arrayOutputStream.write(buffer,0,bytesRead);
		   }
		   arrayOutputStream.close();
		 } catch(Exception e) {}
		 return arrayOutputStream.toByteArray(); 
	}
	
	
	public static void salvarImagem(byte[] imagem, String destinoImagem) {
		try {
			FileOutputStream fos = new FileOutputStream(destinoImagem);  
			fos.write(imagem);  
			FileDescriptor fd = fos.getFD();  
			fos.flush();  
			fd.sync();  
			fos.close();  
		} catch(Exception e ) {}
	}
}
