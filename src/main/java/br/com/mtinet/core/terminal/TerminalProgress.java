package br.com.mtinet.core.terminal;

import br.com.mtinet.core.listener.ProgressListener;

public class TerminalProgress implements ProgressListener{
	private int max = 0;
	private int size = 15;
	
	private int sizeLabel =  18;
	private int sizeNumber = 9; //9
	private int sizeProgress = 15;
	private int sizeMsg = 18; //21
	
	public int  getModuleSize(){
		return 100;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
	public void showNewMessage(String msg) {
		System.out.print("\n" + msg);
	}
	
	public void showMessage(String msg) {
		System.out.print(msg);
	}
	
	public void showTime(String time) {
		System.out.print(time);
	}
	
	public void showVisorOperation(String msg) {
		String msgFormated = String.format("%-"+sizeMsg+"." +sizeMsg+"s","- "+msg.toUpperCase()+ ":" );
		System.out.print("\n" + msgFormated);
	}
	
	public void showProgress(int pos) {
		clearText(15);
		System.out.println(String.format("%-"+size+"." +size+"s",pos + "/" + max));
	}
	
	public void showResulted(int pos) {
		clearText(15);
		System.out.println(String.format("%-"+size+"." +size+"s",pos + "/" + max));
	}
	
	public void clearText(int count) {
		String clear = "";
		for (int c =0 ; c< count ; c++) {
			clear += "\b";
		}
		showMessage(clear);
	}
	
	public void clearLine() {
		int size  = sizeLabel + sizeNumber +sizeProgress +sizeMsg ;
		clearText(size);
	}
}
