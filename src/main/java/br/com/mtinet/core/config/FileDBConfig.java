package br.com.mtinet.core.config;

public abstract class FileDBConfig extends DBConfig {
	
	public FileDBConfig() {
		
	}
	
	public FileDBConfig(String[] args) throws Exception {
		super();
		int size = args.length;
		if (size==5||size==6) {
			this.dataBase = args[2];
			this.user = args[3];
			this.password = args[4];
			this.url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="  + dataBase;
		} else throw new Exception("Parametro invalido:\n   java -jar jdb.jar CONN <DRIVER> <DATABASE> <USER> <PASSWORD> [FILEPATH]");
		
	}

	
}
