package model;

public class PropertyKey {
	
	private String tableName;
	private String columnName;
	
	public PropertyKey(String tableName, String columnName) {
		
		this.tableName = tableName;
		this.columnName = columnName;
	}

	
	@Override
	public boolean equals(Object object) {
		
		if (((PropertyKey) object).tableName == null && ((PropertyKey) object).columnName == null) {
			return true;
		}
		if (((PropertyKey) object).tableName == null
				&& ((PropertyKey) object).columnName.equals(this.columnName)) {
			return true;
		}
		if (((PropertyKey) object).tableName.equals(this.tableName)
				&& ((PropertyKey) object).columnName == null) {
			return true;
		}
		if (((PropertyKey) object).tableName.equals(this.tableName)
				&& ((PropertyKey) object).columnName.equals(this.columnName)) {
			return true;
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		int hashCode = this.tableName == null ? 0 : this.tableName.hashCode();
		hashCode = hashCode + (this.columnName == null ? 0 : this.columnName.hashCode());
		return hashCode;
	}

	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	

}
