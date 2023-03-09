package br.com.mtinet.exception;

public class BuildException extends Exception{
	private String message = "";
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BuildException(String msg) {
		this.message = msg;
	}
}
