package br.com.mtinet.core.terminal;

import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.util.MessageCommon;

public class TerminalProgressColor implements ProgressListener{
	private int max = 0;
	private int maxModuled = -1;
	private int size = 15;
	private int sizeText = 9;
	
	private int sizeLabel =  18;
	private int sizeNumber = 9; //9
	private int sizeProgress = 15;
	private int sizeMsg = 21; //21
	
	public int  getModuleSize(){
		return 10;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	public void setMax(int max) {
		this.max = max;
		this.maxModuled = -1;
	}
	
	public void showVisorOperation(String msg) {
		showNewMessage(msg);
	}
	
	public void showNewMessage(String msg) {
//		System.out.print("\n" + msg);
		String msgFormated = String.format("%-21.21s","- "+msg.toUpperCase()+ ":" );
		String tempo = " Calculando o tempo...";
		String labelPos = MessageCommon.alignText(0, sizeText, MessageCommon.ALIGN_RIGTH);
		String labelMax = MessageCommon.alignText(max, sizeText, MessageCommon.ALIGN_RIGTH); 
		System.out.print("\n" + msgFormated);
		System.out.print(labelPos + String.format("%-"+size+"." +size+"s",  gereProgress(ajuste(0))) +labelMax + tempo );
	}
	
	public void showMessage(String msg) {
		System.out.print(msg);
	}
	
	public void showTime(String time) {
		System.out.print(time);
	}
	
	public void showProgress(int pos)  {
		clearText(size+sizeText);
		String labelPos = MessageCommon.alignText(pos, sizeText, MessageCommon.ALIGN_LEFT);
		String labelMax = MessageCommon.alignText(max, sizeText, MessageCommon.ALIGN_RIGTH); 
		System.out.print(labelPos + String.format("%-"+size+"." +size+"s",  gereProgress(ajuste(pos))) +labelMax );
	}
	
	public void showResulted(int pos) {
		clearText(size);
		int tam = ajuste(pos);
		System.out.println( gereProgress(tam));
	}
	
	public void clearText(int count) {
		String clear = "";
		for (int c =0 ; c< count ; c++) {
			clear += "\b";
		}
		showMessage(clear);
	}
	
	public void clearLine() {
		int size  = sizeLabel + (sizeNumber*2) +sizeProgress +sizeMsg ;
		clearText(size);
	}
	
	private int ajuste(int pos) {
		if (maxModuled==-1) maxModuled = moduleMax();
//		int bloc = moduleMax();
		int perc = 1;
		if (pos> maxModuled) {
			perc =  (int)Math.ceil(pos/maxModuled);
		} else if (pos==max) perc = size;
		return perc;
	}
	
	private int moduleMax() {
		int n = 0;
		if (max>size) n = (int)Math.ceil(max/size);
		else n = max;
		if (n!=0) return  n;
		else return size;
	}
	
	private String  gereProgress(int pos) {
		String text = "";
		int idCaracter = 0;
		String osName = System.getProperty("os.name"); 
		
		if (osName.toUpperCase().contains("WIN")) idCaracter = 177;
		else idCaracter = 42;
		for (int c =0 ; c< pos ; c++) {
			char caracter = (char) idCaracter;  // 177 ou 178
			String ascw = new Character(caracter).toString(); 
			text += ascw;
		}
		return text;
	}
}
