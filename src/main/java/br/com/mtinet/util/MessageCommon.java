package br.com.mtinet.util;

public class MessageCommon {
	
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_RIGTH = 2;
	public static final int ALIGN_LEFT = 3;
	
	public static void showTitle() {
		printLineDuple();
		FileResource.printText(FileResource.getText("app.alias") + "   "+ FileResource.getText("app.name"));
		FileResource.printBundle("app.description");
		FileResource.printText(FileResource.getText("app.version") + "   -   " + FileResource.getText("app.year"));
		FileResource.printBundle("app.author");
		FileResource.printText(FileResource.getText("app.author.tel")+ "   -   " +FileResource.getText("app.author.email"));
		printLineDuple();
		
	}
	
	public static void showMessageHelp() {
		print("   " + FileResource.getText("app.help.title"));
		printLine();
		print(" -  " + FileResource.getText("app.help.commnad.conn"));
		print(" -  " + FileResource.getText("app.help.command.exec"));
		print(" -  " + FileResource.getText("app.help.command.run"));
		print(" -  " + FileResource.getText("app.help.command.scanner"));
		print(" -  " + FileResource.getText("app.help.command.help"));
		printLine();
	}
	
	public static void print(String value) {
		System.out.println(value);
	}
	public static void printLineDuple() {
		print( repeatText("=",76) );
	}
	public static void printLine() {
		print( repeatText("-",76) );
	}
	public static void printLine(int size) {
		print( repeatText("-",size) );
	}
	public static void printLineEnd() {
		print( repeatText("-",76)+"\n" );
	}
	
	public static String repeatText(String text,int count) {
		String value = "";
		for (int c =0 ; c< count ; c++) value += text;
		return value;
	}
	
	public static String alignText(int number, int size,int form)  {
		return alignText(new Integer(number).toString(),size,form);
	}
	
	public static String alignText(String text, int size,int form) {
		switch (form) {
			case 1: return alignCenter(text,size,form);
			case 2: return alignRigth(text,size,form);
			case 3: return alignLeft(text,size,form);
			default: return text;
		}
	}
	
	private static String alignCenter(String text, int size,int form) {
		String value = "";
		int i = (int)Math.ceil(size/2)-(int)Math.ceil(text.length()/2);
		for (int p=1;p<i;p++) value +=" ";
		value += text;
		return String.format("%-"+size+"."+size+"s",value);
	}
	
	private static String alignRigth(String text, int size,int form) {
		String value = "";
		int i = size - text.length();
		for (int p=0;p<i;p++) value +=" ";
		value += text;
		return value;
	}
	
	private static String alignLeft(String text, int size,int form) {
		return String.format("%-"+size+"."+size+"s",text);
	}
}
