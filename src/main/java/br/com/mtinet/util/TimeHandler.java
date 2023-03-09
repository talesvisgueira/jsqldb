package br.com.mtinet.util;

public class TimeHandler {
	
	
	
	public static String logTempo(long inicio, int processado, int total) {
		long restante;
		long agora = System.currentTimeMillis();
		long decorrido = agora - inicio;
		if (processado>0) {
			restante = (decorrido * (total - processado))/processado;
			String tempo = " Restante: " + tempo2String(restante);
			return String.format("%-25.25s",tempo) ;
		} return String.format("%-25.25s","00h, 00m, 00s") ;

	}
	
	public static String tempo2String(long milisegundos) {
		long segundos = milisegundos / 1000;
		long minutos = segundos / 60;
		long horas = (segundos / 60) /60;
		segundos = segundos % 60;
		if (minutos<60) return  horas + "h, " + minutos + "m, " + segundos + "s";
		else {
			minutos = minutos % 60;
			horas = horas %24 ;
			return horas + "h, " + minutos + "m, " + segundos + "s";
		}
		
	}

}
