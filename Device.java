import java.util.*;

public class Device {
	private static ArrayList<Device> devices = new ArrayList<Device>();
	private String deviceName;
	private int currentProcess;
	private boolean locked;
	
	
	Device(String name){
		this.deviceName = name;
		locked = false;
		devices.add(this);
	}
	
	ArrayList<Device> getAllDevices(){
		return devices;
	}
	
	String getName(){
		return deviceName;
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
