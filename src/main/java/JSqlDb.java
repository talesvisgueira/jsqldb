/**
 * Sistema de Gerenciamento de Projetos e Produtos - SGP2
 * Copyright (C) 2008-2011 
 * Criação: 15/11/2008
 */
import java.io.File;


import br.com.mtinet.core.ServiceConnect;
import br.com.mtinet.core.ServiceScanner;
import br.com.mtinet.core.ServiceScript;
import br.com.mtinet.core.listener.WriterListener;
import br.com.mtinet.core.terminal.TerminalWriter;
import br.com.mtinet.util.FileResource;
import br.com.mtinet.util.MessageCommon;
import br.com.mtinet.util.SplashScreen;


/**
 * Classe auxiliar para trabalhar com arquivos
 * @author Tales Visgueira
 * @version 1
 */
public class JSqlDb {
	private static WriterListener writer;
	private static String param;
	
	public static void main(String[] args) {
		writer = new TerminalWriter();
		SplashScreen.show();
		try {	
			criarDiretorios();
//			loadJdbcFilesDriveres() ;
			MessageCommon.showTitle();

			if (args.length>0) {
				param = args[0];
				switch(tranformParameter(param)) {
				   case 1:  ServiceConnect conn = new ServiceConnect(args); 
				   			 conn.execute(); break;
				   case 2:  new ServiceScript(args)  ; break;
				   case 3:  new ServiceScript(args)  ; break;
				   case 4:  new ServiceScanner(args) ; break;
				   case 5:  showMessageHelp(); break;
				   default: FileResource.printBundle("msg.default");
				}	
			} else FileResource.printBundle("msg.default");

		} catch (Exception e) {System.out.println(e.getMessage());}
		
	}

	/**
	 * Método para remover um diretório
	 * @param Objeto File que representa um diretorio
	 */
	private static void criarDiretorios() {
		criarDiretorio("log");
		criarDiretorio("scripts");
		criarDiretorio("audit");
		criarDiretorio("data");
		criarDiretorio("sql");
		criarDiretorio("config");
		criarDiretorio("jdbc");		
	}
	
	private static void criarDiretorio(String directory) {
		File dir = new File(directory); 
		if (!dir.exists()) dir.mkdir();
	}
	
//	private static void loadJdbcFilesDriveres() throws Exception {
//		DynamicDriverManager loader = new DynamicDriverManager();
//		loader.addDir("jdbc");;
//	}
	
	private static int tranformParameter(String value) {
		if (value.toUpperCase().equals("CONN")) return 1;
		else if (value.toUpperCase().equals("EXEC")) return 2;
		else if (value.toUpperCase().equals("RUN")) return 3;
		else if (value.toUpperCase().equals("SCANNER")) return 4;
		else if (value.toUpperCase().equals("HELP")) return 5;
		return 0;
	}

	private static void showMessageHelp() {
		MessageCommon.showMessageHelp();
	}

}
