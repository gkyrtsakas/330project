import java.util.*;

public class Resource {	
	private static ArrayList<Resource> resources = new ArrayList<Resource>();
	private String resourceName;
	private int currentProcess;
	private boolean locked;
	
	
	Resource(String name){
		this.resourceName = name;
		locked = false;
		resources.add(this);
	}
	
	ArrayList<Resource> getAllResources(){
		return resources;
	}
	
	void setLock(int newProcess){
		locked = true;
		this.currentProcess = newProcess;
		
	}
	
	void releaseLock(){
		locked = false;
		this.currentProcess = (Integer)null;
	}
	
	boolean isAvailable(){
		return !locked;
	}

}
