package br.com.mtinet.core.terminal;

import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.util.MessageCommon;

public class TerminalProgressColor2 implements ProgressListener{
	private int max = 0;
	private int maxModuled = -1;
	
	private int sizeLabel =  18;
	private int sizeNumber = 9; //9
	private int sizeProgress = 15;
	private int sizeMsg = 21; //21
	
	public int  getModuleSize(){
		return 10;
	}
	
	public void setSize(int size) {
		this.sizeProgress = size;
	}
	public void setMax(int max) {
		this.max = max;
		this.maxModuled = -1;
	}
	
	public void showVisorOperation(String msg) {
		String msgFormated = String.format("%-"+sizeLabel+"." +sizeLabel+"s","- "+msg.toUpperCase()+ ":" );
		System.out.print("\n" + msgFormated);
	}
	
	
	
	public void showNewMessage(String msg) {
		String text = " Calculando o tempo...";
		String labelPos = MessageCommon.alignText(0, sizeNumber, MessageCommon.ALIGN_RIGTH);
		String labelMax = MessageCommon.alignText(max, sizeNumber, MessageCommon.ALIGN_RIGTH); 
		showVisorOperation(msg);
		showProgress(labelPos,labelMax,text);
	}
	
	
	
	public void showMessage(String msg) {
		System.out.print(msg);
	}
	
	public void showTime(String time) {
		clearText(sizeMsg);
		System.out.print(time);
	}
	
	public void showProgress(int pos)  {		
		String labelPos = MessageCommon.alignText(pos, sizeNumber, MessageCommon.ALIGN_LEFT);
		String labelMax = MessageCommon.alignText(max, sizeNumber, MessageCommon.ALIGN_RIGTH); 
//		System.out.print(labelPos + String.format("%-"+sizeVisor+"." +sizeVisor+"s",  gereProgress(ajuste(pos))) +labelMax );
		showProgress(labelPos,labelMax,gereProgress(ajuste(pos)) +labelMax);
	}
	
	private void showProgress(String labelPos,String labelMax,String tempo) {
		clearText(sizeProgress+(sizeNumber*2)+sizeMsg);
		System.out.print(labelPos + String.format("%-"+sizeProgress+"." +sizeProgress+"s",  gereProgress(ajuste(0))) +labelMax );
		System.out.print(String.format("%-"+sizeMsg+"." +sizeMsg+"s",tempo));
	}
	
	public void showResulted(int pos) {
		clearText(sizeProgress);
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
	
	public void showTimeCount() {
		
	}
	
	public void clearLine() {
		int size  = sizeLabel + sizeNumber +sizeProgress +sizeMsg ;
		clearText(size);
	}
	
	private int ajuste(int pos) {
		if (maxModuled==-1) maxModuled = moduleMax();
//		int bloc = moduleMax();
		int perc = 1;
		if (pos> maxModuled) {
			perc =  (int)Math.ceil(pos/maxModuled);
		} else if (pos==max) perc = sizeProgress;
		return perc;
	}
	
	private int moduleMax() {
		int n = 0;
		if (max>sizeProgress) n = (int)Math.ceil(max/sizeProgress);
		else n = max;
		if (n!=0) return  n;
		else return sizeProgress;
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
