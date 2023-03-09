package br.com.mtinet.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumericFormat {
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_LEFT = 2;
	
	public static String format(int value, String format)	{
		NumberFormat formatter = new DecimalFormat(format);
		return formatter.format(value);
	}
	
	public static String format(long value, String format)	{
		NumberFormat formatter = new DecimalFormat(format);
		return formatter.format(value);
	}
	
	public static String align(long value, int fieldLength, int alignRight)	{
		return align(new Long(value).intValue(),fieldLength,alignRight);
	}
	
	public static String align(int value, int fieldLength, int alignRight)	{
		char padding = ' ';
		String newValue = format(value,"#,###,###");
		int length = fieldLength - newValue.length();
		if (length <= 0) return newValue.substring(0,newValue.length());
		StringBuffer buffer = new StringBuffer(fieldLength);
		for(int i=0; i<length; i++) 
			buffer.append(padding);
		
		switch (alignRight) {
			case 1: buffer.append(newValue); break;
			default: buffer.insert(0,newValue); break;
		}
		return new String(buffer);	
	}
	
	public static String align(String value, int fieldLength, int alignRight)	{
		return align(new Integer(value).intValue(),fieldLength,alignRight);
	}


}
