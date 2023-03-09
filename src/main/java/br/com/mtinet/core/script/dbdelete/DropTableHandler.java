package br.com.mtinet.core.script.dbdelete;

import java.sql.Connection;
import java.sql.ResultSet;

import org.w3c.dom.Node;

import br.com.mtinet.core.dataBase.MetaDataBase;
import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.core.script.Handler;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.MessageCommon;
import br.com.mtinet.util.TimeHandler;

public class DropTableHandler extends AbstractHandler  implements Handler  {

	public DropTableHandler( Node tagNode,PoolConnection poolConn,ProgressListener listener )  throws Exception{
		this.setParameters(tagNode,poolConn, listener);

	}

	public void stop() {
		
	}
	
	public void execute() throws Exception  {
		long tempoInicial  = System.currentTimeMillis();
		String dsnTarget = AttributeLoader.getAttributeValue(tagNode,"dsn",true) ;
		Connection conn = this.poolConn.getConn(dsnTarget).getConn();
		MetaDataBase meta = new MetaDataBase(conn );
		ResultSet tables = meta.getResultTables(schema);
		int tot= 0; int pos =0;
		String sql = "";
		tot = meta.getResultTablesCount(schema);
		this.listener.showNewMessage("DROP "+dsnTarget);
		this.listener.setMax(tot);
		while (tables.next())  {
			sql = "DROP TABLE " +schema+"."+ tables.getString(3);	
			conn.createStatement().executeUpdate(sql);
			conn.commit();
			listener.clearText(9);
			listener.showProgress(pos++);
		}
		
		listener.clearText(31);
		this.listener.showProgress(tot);
		listener.showMessage(this.tempoTotal(tempoInicial));
		System.out.println("");
		MessageCommon.printLine();
	}
	
	private String tempoTotal(long inicio ) {
		long agora = System.currentTimeMillis();
		long decorrido = agora - inicio;
		return " Tempo total: " + TimeHandler.tempo2String(decorrido);
	}
	

}
