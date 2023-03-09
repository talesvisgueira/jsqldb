package br.com.mtinet.core.script;

public interface Handler {
	public static final String TAG_TYPE_DBMIG = "DBMIG";
	public static final String TAG_TYPE_DBDDL = "DBDDL";
	public static final String TAG_TYPE_DBREPLICATE = "DBREPLICATE";
	public static final String TAG_TYPE_DBSINCRONIZE = "DBSINCRONIZE";
	public static final String TAG_TYPE_DBIMPORT = "DBIMPORT";
	public static final String TAG_TYPE_DBEXPORT = "DBEXPORT";
	public static final String TAG_TYPE_DBRANDOM = "DBRANDOM";
	public static final String TAG_TYPE_DBAUDIT = "DBAUDIT";
	public static final String TAG_TYPE_DBREPORT = "DBREPORT";
	public static final String TAG_TYPE_DBDELETE = "DBDELETE";
	public static final String TAG_TYPE_DBDROP = "DBDROP";
	public static final String TAG_TYPE_EXECUTEFILE = "EXECFILE";
	
	public void execute() throws Exception;
	public void stop();
}
