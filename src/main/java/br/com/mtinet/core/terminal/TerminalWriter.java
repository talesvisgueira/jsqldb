package br.com.mtinet.core.terminal;

import br.com.mtinet.core.listener.WriterListener;

public class TerminalWriter implements WriterListener {
	
	public void showMessage(String msg) {
		System.out.println(msg);
	}
}
