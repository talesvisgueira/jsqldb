package br.com.mtinet.core.scanner;


public class Service {
	private Long   id;
	private String name;
	private String description;
	private String type;
	private boolean client;
	
	public Service() {
		
	}
	public Service(Long id) {
		super();
		this.id = id;
	}
	public Service(Long id,String type,String name,String description, boolean client) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type ;
		this.client = client;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isClient() {
		return client;
	}
	public void setClient(boolean client) {
		this.client = client;
	}
	
	
}
