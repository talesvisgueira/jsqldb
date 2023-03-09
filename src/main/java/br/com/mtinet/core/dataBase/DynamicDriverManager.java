package br.com.mtinet.core.dataBase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;

import br.com.mtinet.core.connect.ConnectionConfig;

public class DynamicDriverManager {
	private ArrayList<URL> urls = new ArrayList<URL>();

	public ConnectionConfig createConnection(ConnectionConfig config)  throws Exception {
		Connection conn = this.createConnection(config.getDriver(), config.getUrl(),config.getUser(),config.getPassword());
		if (!config.getDriver().contains("LDAP"))
			conn.setAutoCommit(false);
		config.setConn(conn);
		return config;	
	}
	
	public Connection createConnection(String driverName, String url, String user, String password) throws Exception {
		URLClassLoader syslogger =  (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class newClass = syslogger.loadClass(driverName);
		Driver driverClass = (Driver) newClass.newInstance();
		if (driverClass instanceof java.sql.Driver ) {
			  DriverManager.registerDriver(driverClass);
		}
		if (driverName.contains("LDAP")) {
			return driverClass.connect(url+";user="+user+";password="+password+";useCleartext=true",null);
		} else return DriverManager.getConnection(url,user,password);
	}

	public void addDir(String dirPath) throws Exception {
		File dir = new File(dirPath);
        String[] children = dir.list();
        if (children != null) {
        	for (int i=0; i<children.length; i++) {
        		String filename = dir.getAbsoluteFile()+"//"+children[i];
                File file = new File(filename);
                if (file.exists()) 	this.addFile(file);
        	}	
        } else throw new Exception("AVISO: Nao existe drivers JDBC no diretorio '" +dirPath +"'.");
	}

	private static final Class[] parameters = new Class[]{
		URL.class
	}; 
	
	public  void addFile(String s) throws IOException {
		File f = new File(s); 
		addFile(f);        
	}//end method  
	
	public  void addFile(File f) throws IOException { 
		addURL(f.toURL());
	}//end method
	
	public  void addURL(URL u) throws IOException { 
		if (!urlLoaded(u)) {
			
			URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader(); 
			//URLClassLoader sysloader = (URLClassLoader)Thread.currentThread().getContextClassLoader(); 
			Class sysclass = URLClassLoader.class; 
			try {  
				Method method = sysclass.getDeclaredMethod("addURL",parameters);  
				method.setAccessible(true); 
				method.invoke(sysloader,new Object[]{ u });
			} catch (Throwable t) { 
				t.printStackTrace();
				throw new IOException("Error, could not add URL to system classloader");
			}//end try catch        }//end method}
			urls.add(u);
		}
		
	}
	
	private  boolean urlLoaded(URL u) {
		boolean value = false;
		for (URL url : urls) {
			if (url.getFile().equals(u.getFile()))
				return true;
		}
		return value;
	}
}
