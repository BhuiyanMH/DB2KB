package model;

public class DBParams {
	
	private String dbName, dbUserName, dbUserPassword;

	public DBParams(){
		dbName = "";
		dbUserName = "";
		dbUserName = "";
	}
	public DBParams(String dbName, String dbUserName, String dbUserPassword) {
		this.dbName = dbName;
		this.dbUserName = dbUserName;
		this.dbUserPassword = dbUserPassword;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbUserName() {
		return dbUserName;
	}
	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}
	public String getDbUserPassword() {
		return dbUserPassword;
	}
	public void setDbUserPassword(String dbUserPassword) {
		this.dbUserPassword = dbUserPassword;
	}
	

}
