package br.com.mtinet.core.scanner;

import java.util.ArrayList;
import java.util.List;


public class Host {
	private Long   id;
	private String alias;
	private String dominio;
	private String ip;
	private String description;
	private String type;
	private String system;
	private String systemVersion;
	private List<Service> serives;
	private String memory;
	private String virtual;
	
	public Host() {
		
	}
	
	public Host(Long id) {
		super();
		this.id = id;
	}
	
	public Host(Long id, String alias,String ip,String dominio, String type,String system,String version) {
		super();
		this.id = id;
		this.alias = alias.toUpperCase();
		this.ip = ip;
		this.dominio = dominio;
		this.type = type;
		this.system = system;
		this.systemVersion = version;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}	
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getVirtual() {
		return virtual;
	}
	public void setVirtual(String virtual) {
		this.virtual = virtual;
	}
	public List<Service> getSerives() {
		if (serives==null) this.serives = new ArrayList<Service>();
		return serives;
	}
	public void setSerives(List<Service> serives) {
		this.serives = serives;
	}
	
	

}
