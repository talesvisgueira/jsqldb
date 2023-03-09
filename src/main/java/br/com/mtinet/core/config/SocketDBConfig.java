package br.com.mtinet.core.config;

public abstract class SocketDBConfig extends DBConfig {
	
	public SocketDBConfig() {
		
	}

	public SocketDBConfig(String[] args) throws Exception {
		int size = args.length;
		if (size==7||size==8) {
			this.host = args[2];
			this.port = args[3];
			this.dataBase = args[4];
			this.user = args[5];
			this.password = args[6];	
 
		} else throw new Exception("Parametro invalido:\n   java -jar jdb.jar CONN <DRIVER> <HOST> <PORT> <DATABASE> <USER> <PASSWORD> [FILEPATH]");
		
	}
}
