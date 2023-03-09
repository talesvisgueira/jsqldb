package br.com.mtinet.core.scanner;

public class IPHander {
	private String ip="";
	private String n1="";
	private String n2="";
	private String n3="";
	private String n4="";
	
	public IPHander(String ip) throws Exception {
		this.ip = ip.trim();
		String ipTemp = ip.trim();
		try {
			n1 = ipTemp.substring(0, ipTemp.indexOf("."));
			ipTemp = ipTemp.substring(ipTemp.indexOf(".")+1,ipTemp.length());
			
			n2 = ipTemp.substring(0,ipTemp.indexOf("."));
			ipTemp = ipTemp.substring(ipTemp.indexOf(".")+1,ipTemp.length());
			
			n3 = ipTemp.substring(0,ipTemp.indexOf("."));
			ipTemp = ipTemp.substring(ipTemp.indexOf(".")+1,ipTemp.length());
			
			n4 = ipTemp.substring(0,ipTemp.length());
		} catch(Exception e) {throw new Exception("Erro:  IP invalido");}
		validIP();
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getN1() {
		return n1;
	}
	public void setN1(String n1) {
		this.n1 = n1;
	}
	public String getN2() {
		return n2;
	}
	public void setN2(String n2) {
		this.n2 = n2;
	}
	public String getN3() {
		return n3;
	}
	public void setN3(String n3) {
		this.n3 = n3;
	}
	public String getN4() {
		return n4;
	}
	public void setN4(String n4) {
		this.n4 = n4;
	}
	
	private void validIP() throws Exception {
		if (n1=="") throw new Exception("Erro:  IP "+ip+" invalido");
		if (n2=="") throw new Exception("Erro:  IP "+ip+" invalido");
		if (n3=="") throw new Exception("Erro:  IP "+ip+" invalido");
		if (n4=="") throw new Exception("Erro:  IP "+ip+" invalido");
		
		if (new Integer(n1).intValue()>255) throw new Exception("Erro:  IP "+ip+" invalido");
		if (new Integer(n2).intValue()>255) throw new Exception("Erro:  IP "+ip+" invalido");
		if (new Integer(n3).intValue()>255) throw new Exception("Erro:  IP "+ip+" invalido");
		if (new Integer(n4).intValue()>255) throw new Exception("Erro:  IP "+ip+" invalido");
	}
	
	public int getIpNumber() {
		int number = 0;
		int v1,v2,v3,v4 = 0;
		if(n4.equals("0")) v4 = 0 ;else v4 = new Integer(n4).intValue();
		if(n3.equals("0")) v3 = 0 ;else v3 = new Integer(n3).intValue()+255;
		if(n2.equals("0")) v2 = 0 ;else v2 = new Integer(n2).intValue()+255*2;
		if(n1.equals("0")) v1 = 0 ;else v1 = new Integer(n1).intValue()+255*6;
		number = v1+v2+v3+v4;
		return number;
		
	}

}
