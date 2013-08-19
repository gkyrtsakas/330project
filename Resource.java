import java.util.*;

public class Resource {	
	private static ArrayList<Resource> resources = new ArrayList<Resource>();
	private static int count = 0;
	private String portID;
	private String resourceName;
	private Process currentProcess;
	private boolean locked;
	
	// Constructor
	public Resource(String name){
		this.resourceName = name;
		locked = false;
		resources.add(this);
		count ++;
		portID = "R-" + count;
	}
	// returns all resources created as of the moment
	static ArrayList<Resource> getAllResources(){
		return resources;
	}
	// returns the resource in the specified port
	// if found. Else, prints an error string.
	static Resource findResource(String resourcePort){
		int index = Integer.parseInt((resourcePort.split("-"))[1]);
		if(index <= count){
			return resources.get(index - 1);
		}
		else{
			System.out.println("Error: There is no device with this port");
			return null;
		}
	}
	// Prints all resources in a pretty table
	static String getTableInfo(){
		String isLocked = "", processName = "", resourceName = "", portID = "", 
		returnString = "|------Resource Name------|---Port ID---|-----Current Process-----|--Locked--|";
		for(int x=0; x < count; x++){
			Resource r = resources.get(x);
			// format locked string
			if(r.isAvailable()){
				isLocked = "Available.";
			}
			else{
				isLocked = "Locked....";
			}
			// format Device name string
			resourceName = r.resourceName;
			for(int i = resourceName.length(); i < 25; i++){
				resourceName += ".";
			}
			
			// format port ID string
			portID = r.portID;
			for(int i = portID.length(); i < 13; i++){
				portID += ".";
			}
		
			// format process name string
			if(r.currentProcess != null){
				processName = r.currentProcess.getName();
				for(int i = processName.length(); i < 25; i++){
					processName += ".";
				}
			}
			else{
				processName = ".........................";
			}
			
			
			returnString += "\n|" + resourceName + "|" + portID + "|" + processName + "|" + isLocked + "|";
		}
		return returnString;
	}
	// Sets the lock and displays the process currently being used
	void setLock(Process newProcess){
		if(!locked){
			locked = true;
			this.currentProcess = newProcess;
		}
		else{
			System.out.println("Error: Locked by Process " + currentProcess.getName());
		}
	}
	// Releases lock when the process is complete
	void releaseLock(){
		locked = false;
		this.currentProcess = (Process)null;
	}
	// returns boolen to determine if the lock is available
	boolean isAvailable(){
		return !locked;
	}
	// returns port ID
	String getPortID(){
		return this.portID;
	}
}
