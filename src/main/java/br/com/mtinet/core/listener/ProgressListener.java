package br.com.mtinet.core.listener;

public interface ProgressListener {
	public void setMax(int max);
	public void showVisorOperation(String msg);
	public void showMessage(String msg);
	public void showTime(String time);
	public void showNewMessage(String msg);
	public void showProgress(int pos);
	public void clearText(int count) ;
	public void clearLine();
	public void showResulted(int pos);
	public void setSize(int size) ;
	public int  getModuleSize();
}
