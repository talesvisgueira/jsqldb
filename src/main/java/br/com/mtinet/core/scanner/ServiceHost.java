package br.com.mtinet.core.scanner;


public class ServiceHost {
	private Long id;
	private Host host;
	private Service service;
	private int port;
	
	public ServiceHost() {
		
	}
	public ServiceHost(Long id) {
		super();
		this.id = id;
	}
	public ServiceHost(Long id,Host host, Service service, int port) {
		super();
		this.id = id;
		this.host = host;
		this.service = service;
		this.port = port;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Host getHost() {
		return host;
	}	
	public void setHost(Host host) {
		this.host = host;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

}
