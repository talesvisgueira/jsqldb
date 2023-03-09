 /**
 * Repositório de Projetos e Produtos - RP2
 * Copyright (C) 2008  Tales Anaximandro do Bonfim Visgueira
 * Class AdapterClassName
 * Criação: 15/11/2008
 */
package br.com.mtinet.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Classe auxiliar para tratamento de data
 * @author Tales Visgueira
 * @version 1
 */
public class DateUtil {
	Date dataNow = new Date();
	String mesesNum[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};
	String mesesSig[] = {"Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez"};
    String meses[] = {"janeiro","fevereiro","mar�o","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"};
    String semana[] = {"Domingo","Segunda-feira","Ter�a-feira","Quarta-feira","Quinta-feira","Sexta-feira","S�bado"};

    
    public DateUtil() {

    }
    
    public DateUtil(String data) {
    	dataNow = new Date(data);
    }
    
    public GregorianCalendar getGregorianData() {    	
    	GregorianCalendar data = new GregorianCalendar(dataNow.getYear(), dataNow.getMonth(), dataNow.getDay());
    	return data;
    }
    /**
	 * Método
	 * @param 
	 * @return 
	 */
    public void setDate(Date data){
    	this.dataNow = data;
    }
    
    /**
	 * Método
	 * @param 
	 * @return 
	 */
    
    @SuppressWarnings("deprecation")
	public void setDate(String data){
    	this.dataNow = new Date(data);
    }
    
    /**
	 * Método
	 * @param 
	 * @return 
	 */
    @SuppressWarnings("deprecation")
	public boolean isDateValid(String data) {
		return isDateValid( new Date(data));
	}
	
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
    @SuppressWarnings("deprecation")
	public boolean isDateValid(Date data) {
		boolean resultado = false;
		if ((data.getDate() < 31) && (data.getMonth() < 12))
			resultado = true;
		return resultado;
	}
	
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
    @SuppressWarnings("deprecation")
	public String getDataInvertida() {
	
	    int dia = dataNow.getDate();
	    int mes = dataNow.getMonth();
	    int ano = dataNow.getYear()+1900;
	    String diaext = ano + "_" + mesesSig[mes] + "_" + dia;
	    return diaext;
    }	
	
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
    @SuppressWarnings("deprecation")
	public String getDataAbreviada(){
	    int dia = dataNow.getDate();
	    int mes = dataNow.getMonth();
	    int ano = dataNow.getYear()+1900;
	    String diaext = dia + " " + mesesSig[mes] + " " + ano;
	    return diaext;
    }	
	
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	public String getNrRefMes(int mes) {
		return mesesSig[mes];
	}
	
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	@SuppressWarnings("deprecation")
	public String getDataExtenso() {
	    int dia = dataNow.getDate();
	    int mes = dataNow.getMonth();
	    int ano = dataNow.getYear()+1900;
	    String diaext = dia + " de " + meses[mes] + " de " + ano;
	    return diaext;
    }		
	
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	public String getsDataEventoExtenso(String sDataEvento) {
	  //a data vem do banco com formato AAAA-DD-MM
	                java.util.Locale locale = new java.util.Locale("pt","BR");
	                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", locale);
	                String sDataAux = sDataEvento;
	                try {
	                    Date myDate = df.parse(sDataAux);
	                    df.applyPattern("dd 'de' MMMM 'de' yyyy '-' EEEE.");
	                    sDataAux = df.format(myDate);
	                } catch( Exception e) {
	                  e.printStackTrace();
	                }
	                return sDataAux;
	 }
	
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	public  java.util.Date convertStringtoDate(String data) {
	    GregorianCalendar cal = new GregorianCalendar();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt","br"));
	    long timestamp = 0;
			try{
        timestamp = dateFormat.parse(data).getTime();
  			cal.setTime(new java.util.Date( timestamp ));	 					
			}catch(Exception e) { }; 
  		return cal.getTime();
	 }	
		
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	 public  Timestamp convertDatetoTimestamp(String data) {
	    GregorianCalendar cal = new GregorianCalendar();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt","br"));
	    long timestamp = 0;
			try{
        timestamp = dateFormat.parse(data).getTime();
  			cal.setTime(new java.util.Date( timestamp ));	 					
			}catch(Exception e) { }; 
  		return new Timestamp( cal.getTime().getTime());	 
	 }
	 
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	 public  Timestamp convertDatetoTimestamp(java.util.Date data) {
	 GregorianCalendar cal = new GregorianCalendar();
	    long timestamp = 0;
			try{
        timestamp = data.getTime();
  			cal.setTime(new java.util.Date( timestamp ));	 					
			}catch(Exception e) { }; 
  		return new Timestamp( cal.getTime().getTime());	 
	 }
	 
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	 public  String convertDatetoString(java.util.Date data){
	   SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
	    return formatador.format(data); 
	 }
	 
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	 public  String convertTimestamptoString(Timestamp data){
	   SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
	   return formatador.format(data); 
	 }

	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	 public  java.util.Date getCurrentDate(){
	    return dataNow; 
	 }
	 
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	 public  Timestamp getCurrentTimestamp() {
			return new Timestamp(System.currentTimeMillis() );	 
	 }
	 
	 /**
	 * Método
	 * @param 
	 * @return 
	 */
	 @SuppressWarnings("deprecation")
	 public String acresentarMesData(String dataString) {
		  java.util.Date data =convertStringtoDate(dataString);
		  Timestamp newData = this.convertDatetoTimestamp(data) ;
		  int mes = 1;
		  if (newData.getMonth()==12) {
			 newData.setYear( new Integer(newData.getYear()).intValue()+1 ); 
			 newData.setMonth(mes);
		  } else {
			  mes = newData.getMonth() +1;
			  newData.setMonth(mes); 
		  }
		  return convertTimestamptoString(newData);
	 }
	 
	 public String acresentarDiasData(int dias) {
		  long dia = dataNow.getTime() ;
		  dia = dia + (3600*dias*24*1000);
		  dataNow.setTime(dia);
		  return convertDatetoString(dataNow);
	 }
	 
	 @SuppressWarnings("deprecation")
	public String acresentarDiasUteisData(int dias) {
		  long time = dataNow.getTime() ;
		  for (int dia = 0 ;dia < dias;dia++) {
			  time = time + (3600*1*24*1000);
			  dataNow.setTime(time);
			  if ((dataNow.getDay()==0) ||(dataNow.getDay()==6)) {
				  dia--;
			  }
		  }
		  
		  return convertDatetoString(dataNow);
	 } 
	 
	 
	 
	 

}
