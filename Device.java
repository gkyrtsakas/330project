import java.util.*;

public class Device {
	protected static ArrayList<Device> devices = new ArrayList<Device>();
	protected static int count = 0;
	protected String portID;
	protected String deviceName;
	protected Process currentProcess;
	protected boolean locked;
	//Constructor
	public Device(String name){
		this.deviceName = name;
		locked = false;
		devices.add(this);
		count ++;
		portID = "D-" + count;
	}
	// Returns all devices creates as of moment
	static ArrayList<Device> getAllDevices(){
		return devices;
	}
	// returns string containing a prettily formatted table with device info
	static String getTableInfo(){
		String isLocked = "", processName = "", deviceName = "", portID = "", 
		returnString = "|-------Device Name-------|---Port ID---|-----Current Process-----|--Locked--|";
		for(int x=0; x < count; x++){
			Device d = devices.get(x);
			// format locked string
			if(d.isAvailable()){
				isLocked = "Available.";
			}
			else{
				isLocked = "Locked....";
			}
			// format Device name string
			deviceName = d.deviceName;
			for(int i = deviceName.length(); i < 25; i++){
				deviceName += ".";
			}
			
			// format port ID string
			portID = d.portID;
			for(int i = portID.length(); i < 13; i++){
				portID += ".";
			}
		
			// format process name string
			if(d.currentProcess != null){
				processName = d.currentProcess.getName();
				for(int i = processName.length(); i < 25; i++){
					processName += ".";
				}
			}
			else{
				processName = ".........................";
			}
			
			
			returnString += "\n|" + deviceName + "|" + portID + "|" + processName + "|" + isLocked + "|";
		}
		return returnString;
	}
	// Looks for a device with a specified port ID
	// If one exists it returns the device
	// Else, it displays an error message
	static Device findDevice(String devicePort){
		int index = Integer.parseInt((devicePort.split("-"))[1]);
		if(index <= count){
			return devices.get(index - 1);
		}
		else{
			System.out.println("Error: There is no device with this port");
			return null;
		}
	}
	// Return device's name
	String getName(){
		return deviceName;
	}
	// Sets lock when a process is using it so that it
	// cannot be used by other processes at the same time
	void setLock(Process newProcess){
		if(!locked){
			locked = true;
			this.currentProcess = newProcess;
		}
		else{
			System.out.println("Error: Locked by process " + currentProcess);
		}
	}
	// Releases lock when the process is done so that it can 
	// be used by other processes
	void releaseLock(){
		locked = false;
		this.currentProcess = (Process)null;
	}
	// returns whether or not a device is available
	boolean isAvailable(){
		return !locked;
	}
	// returns the port id of the device
	String getPortID(){
		return this.portID;
	}
}
