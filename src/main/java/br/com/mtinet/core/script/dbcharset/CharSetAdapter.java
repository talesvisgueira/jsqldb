package br.com.mtinet.core.script.dbcharset;

import java.io.UnsupportedEncodingException;

public class CharSetAdapter {
	
	 public String toUTF8(String isoString) {   
		     String utf8String = null;   
		     if (null != isoString && !isoString.equals(""))   
		     {   
		         try  {   
		             byte[] stringBytesISO = isoString.getBytes("ISO-8859-1");   
		             utf8String = new String(stringBytesISO, "UTF-8");   
		         }   
		         catch(UnsupportedEncodingException e) {   
		             // Mostra exce��o mas devolve a mesma String   
		             System.out.println("UnsupportedEncodingException: " + e.getMessage());   
		             utf8String = isoString;   
		         }   
		     }   else   {   
		         utf8String = isoString;   
		     }   
		     return utf8String;   
		 }  

}
