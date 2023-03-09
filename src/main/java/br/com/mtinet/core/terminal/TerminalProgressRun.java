package br.com.mtinet.core.terminal;

import br.com.mtinet.core.listener.ProgressListener;

public class TerminalProgressRun implements ProgressListener{
	private int max = 0;
	private int size = 15;
	private String msgActive;
	
	private int sizeLabel =  18;
	private int sizeNumber = 9; //9
	private int sizeProgress = 15;
	private int sizeMsg = 18; //21
	
	public int  getModuleSize(){
		if (max<10) return 1;
		else if (max<100) return 10;
		else if (max<1000) return 100;
		else if (max<10000) return 1000;
		else if (max<100000) return 10000;
		else if (max<1000000) return 10000;
		else return 100000;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
	public void showVisorOperation(String msg) {
		String msgFormated = String.format("%-"+sizeMsg+"." +sizeMsg+"s","- "+msg.toUpperCase()+ ":" );
		System.out.print("\n" + msgFormated);
	}
	
	public void showNewMessage(String msg) {
		msgActive = msg;
//		System.out.println("\n" + msg);
	}
	
	public void showMessage(String msg) {
		msgActive = msg;
//		System.out.println(msg);
	}
	
	public void showTime(String time) {
//		System.out.print(time);
	}
	
	public void showProgress(int pos) {
		System.out.println(msgActive + " " + String.format("%-"+size+"." +size+"s",pos + "/" + max));
	}
	
	public void showResulted(int pos) {
//		System.out.println(msgActive + " " + String.format("%-"+size+"." +size+"s",pos + "/" + max));
	}
	
	public void clearText(int count) {
//		String clear = "";
//		for (int c =0 ; c< count ; c++) {
//			clear += "\b";
//		}
//		showMessage(clear);
	}
	
	public void clearLine() {
		int size  = sizeLabel + sizeNumber +sizeProgress +sizeMsg ;
		clearText(size);
	}
	
	
}
