package project;

public class Frame {
	private String someData;
	
	public Frame (String s, int pid){
		this.someData = s;
	}
	
	public String getData (){
		return this.someData;
	}
	
	public void setData (String s){
			this.someData = s;
	}

}