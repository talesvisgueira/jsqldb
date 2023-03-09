package br.com.mtinet.core.script.dbddl;


public class DDLObject {
	private String type;
	private String name;
	private String script;		
	
	public DDLObject(String type,String name, String script) {
		this.type = type;
		this.name = name;
		this.script = script;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
	
	
}