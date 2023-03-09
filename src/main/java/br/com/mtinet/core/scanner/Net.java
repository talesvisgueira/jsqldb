package br.com.mtinet.core.scanner;

import java.util.ArrayList;
import java.util.List;


public class Net {
	private Long   id;
	private String name;
	private String description;
	private String ip_min;
	private String ip_max;
	private List<Host>   hosts;
	
	public Net() {
		
	}
	public Net(Long id) {
		super();
		this.id = id;
	}
	public Net(Long id,String name,String description, String ip_min, String ip_max) throws Exception{
		super();
		this.id = id;
		this.name = name;
		this.ip_min = ip_min;
		this.ip_max = ip_max;
		this.description = description;
		validIps();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	public String getIp_min() {
		return ip_min;
	}
	public void setIp_min(String ipMin) {
		ip_min = ipMin;
	}
	public String getIp_max() {
		return ip_max;
	}
	public void setIp_max(String ipMax) {
		ip_max = ipMax;
	}
	public List<Host> getHosts() {
		if (hosts==null) this.hosts = new ArrayList<Host>();
		return hosts;
	}
	public void setHosts(List<Host> hosts) {
		this.hosts = hosts;
	}
	
	public int getHostsCount() {
		int cont = 0;
		if (hosts!=null) {
			for (Host obj : hosts) {
				if (obj!=null) cont++;
			}
		}		
		return cont;
	}
	
	private void validIps() throws Exception{
		IPHander ipMax = new IPHander(ip_max) ;
		IPHander ipMin = new IPHander(ip_min) ;
		if(!(ipMax.getIpNumber()>ipMin.getIpNumber())) throw new Exception("Erro:  Ip Max e menor que o IP Min");
	}
	
	
}
