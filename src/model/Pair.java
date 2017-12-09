package model;

public class Pair {
	Object key, value;

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	boolean checkEmptyKeyValue(){
		
		if(key == null || value == null || key.toString().equals("") || value.toString().equals("")){
			return false;
		}else{
			return true;
		}
	}
}
