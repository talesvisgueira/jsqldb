package br.com.mtinet.core.dataBase;


public  class DelimiterType extends EnumeratedAttribute {
    public static final String NORMAL = "normal";
    public static final String ROW = "row";
    
    public static String[] getValues() {
        return new String[] {NORMAL, ROW};
    }
  }
