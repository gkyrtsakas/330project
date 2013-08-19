import java.util.ArrayList;
import java.util.Vector;

public class Process{
	
	private static String PROCESS_STATE_NEW = "new";
	private static String PROCESS_STATE_READY = "ready";
	private static String PROCESS_STATE_WAITING = "waiting";
	private static String PROCESS_STATE_RUNNING = "running";
	private static String PROCESS_STATE_TERMINATED = "terminated";
	
	protected ArrayList<Process> childProcess = new ArrayList<Process>();
	private int pid;								//unique id of the current process
	private String name;							//name of the current process
	private int priority;							//the priority of the current process (higher value -> higher priority)
	private int submitTime;
	private int burstCycles;
	private String[] requiredDevices;				//list of devices required by the current process
	private ArrayList<Device> usedDevices;			//list of accessed devices in the current process
	private String[] requiredResources;				//list of resources required by the current process
	private ArrayList<Resource> usedResources;		//list of accessed resources in the current process
	private String state;							//current state of the current process
	private static int maxPid = 1;
	private boolean[] flag;

	/** 
	 * Constructs a process
	 * 
	 * @param pid	process identifier
	 * @param name	process name	
	 * @param priority	process priority
	 * @param submitTime	process initial time. (entering ready queue)
	 * @param periodic	true means endless process that repeats burst cycle indefinitely, otherwise	only once
	 * @param burstsCycle process bursts vector. CPU or I/O bursts, values 0 or 1  
	 */
	public Process(int pid, String name, int priority, int submitTime, int burstsCycle, String requiredDevices, String requiredResources) {
		this.pid = pid;
		this.name = name;
		this.priority = priority;
		this.submitTime = submitTime;
		this.burstCycles = burstsCycle;
		this.requiredDevices = requiredDevices.split(" ");
		this.requiredResources = requiredResources.split(" ");
		this.usedDevices = new ArrayList<Device>();
		this.usedResources = new ArrayList<Resource>();
		maxPid++;
		flag = new boolean[pid];
	}
	
	//Returns the current process id
	public int getPid(){
		return this.pid;
	}
	
	//Returns the current process name
	public String getName(){
		return this.name;
	}
	
	//Returns the current process priority
	public int getPriority(){
		return this.priority;
	}
	
	//Returns the current process submission time
	public int getSubmitTime (){
		return this.submitTime;
	}
	
	public int getBurstsCycle() {
		return this.burstCycles;
	}
	
	public void setBurstCycle(int burst) {
		this.burstCycles = burst;
	}
	
	public void setState(String pState)
	{
		//Changes process state based on where it has been moved to.
		if (pState.equals(PROCESS_STATE_NEW))
		{
			this.state = PROCESS_STATE_NEW;
		}
		else if (pState.equals(PROCESS_STATE_READY))
		{
			this.state = PROCESS_STATE_READY;
		}
		else if (pState.equals(PROCESS_STATE_RUNNING))
		{
			this.state = PROCESS_STATE_RUNNING;
		}
		else if (pState.equals(PROCESS_STATE_WAITING))
		{
			this.state = PROCESS_STATE_WAITING;
		}
		else if (pState.equals(PROCESS_STATE_TERMINATED))
		{
			this.state = PROCESS_STATE_TERMINATED;
		}
		
		//If it does not match a clause above, then leave state unmodified
	}
	
	//Returns the current process state
	public String getState(){
		return this.state;
	}
	
	//Returns the unique process identifier
	public static int getMaxPid() {
		return maxPid;
	}
	
	//Determines if the passed device is required by this process
	private boolean isDeviceRequired(Device device){
		boolean found = false;
		for(int i = 0; i < requiredDevices.length; i++){
			if(Device.findDevice(requiredDevices[i]) != null){
				found = true;
				break;
			}
		}
		return found;
	}
	
	//Determines if the passed resource is required by this process
	private boolean isResourceRequired(Resource resource){
		boolean found = false;
		for(int i = 0; i < requiredResources.length; i++){
			if(Resource.findResource(requiredResources[i]) != null){
				found = true;
				break;
			}
		}
		return found;
	}
	
	//Adds a device that the current process may access
	public void addDevice(String deviceName){
		Device device = new Device(deviceName);						//create new device
		if(!isDeviceRequired(device)){
			return;
		}
		
		if(device.isAvailable()){									//check availability
			usedDevices.add(device);								//add device to list
			device.setLock(this);									//lock device
			this.state = PROCESS_STATE_READY;
		}
		else{
			this.state = PROCESS_STATE_RUNNING;
			this.state = PROCESS_STATE_WAITING;
		}
	}
	
	//Removes a device from the current process
	public void removeDevice(Device device){
		for(Device dev : usedDevices){								//locate device in used devices
			if(dev == device){
				usedDevices.remove(usedDevices.indexOf(dev));		//remove device
				dev.releaseLock();									//unlock device
			}
		}
	}
	
	//Unlocks all devices currently used by this process
	public boolean removeAllDevices(){
		try{
			for(Device dev : usedDevices){								//locate device in used devices
				usedDevices.remove(usedDevices.indexOf(dev));			//remove device
				dev.releaseLock();										//unlock device
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
		
	//Returns a specific device from the current process
	public Device getDevice(Device device){
		for(Device dev : usedDevices){								//locate device in used devices
			if(dev == device){
				return dev;											//return found device
			}
		}
		return null;												//else return null
	}
	
	//Returns True if all required devices are accessible
	public boolean getDevicesAvailable(){
		boolean found = true;
		
		for(int i = 0; i < requiredDevices.length; i++){
			if(!Device.findDevice(requiredDevices[i]).isAvailable()){
				found = false;
				removeAllResources();
				break;
			}
		}
		
		if(found){
			for(int j = 0; j < requiredDevices.length; j++){
				Device.findDevice(requiredDevices[j]).setLock(this);
			}
		}
		
		return found;
	}
	
	//Returns all devices accessed by this process as an arraylist
	public ArrayList<Device> getAllDevice(){
		return usedDevices;
	}
	
	//Adds a resource that the current process may access
	public void addResource(String resourceName){
		Resource resource = new Resource(resourceName);				//create new resource
		if(!isResourceRequired(resource)){
			return;
		}
		if(resource.isAvailable()){									//check availability		
			usedResources.add(resource);							//add device to list
			resource.setLock(this);									//lock device
			this.state = PROCESS_STATE_READY;
		}
		else{
			this.state = PROCESS_STATE_RUNNING;
			this.state = PROCESS_STATE_WAITING;
		}
	}
	
	//Removes a resource from the current process
	public void removeResource(Resource resource){
		for(Resource res : usedResources){							//locate resource in used devices
			if(res == resource){
				usedResources.remove(usedResources.indexOf(res));	//remove resource
				res.releaseLock();									//unlock resource
			}
		}
	}
	
	//Unlocks all resources currently used by this process
	public boolean removeAllResources(){
		try{
			for(Resource res : usedResources){							//locate resource in used resources
				usedResources.remove(usedResources.indexOf(res));		//remove resource
				res.releaseLock();										//unlock resource
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	//Returns a specific resource from the current process
	public Resource getResource(Resource resource){
		for(Resource res : usedResources){							//locate resource in used resources
			if(res == resource){
				return res;											//return found resource
			}
		}
		return null;												//else return null
	}
	
	//Returns True if all required resources are accessible
	public boolean getResourcesAvailable(){
		boolean found = true;
		
		for(int i = 0; i < requiredResources.length; i++){
			if(!Resource.findResource(requiredResources[i]).isAvailable()){
				found = false;
				removeAllDevices();
				break;
			}
		}
		
		if(found){
			for(int j = 0; j < requiredResources.length; j++){
				Resource.findResource(requiredResources[j]).setLock(this);
			}
		}
		
		return found;
	}
	
	//Returns an array of all required resources
	public String[] getRequiredDevices(){
		return this.requiredDevices;
	}
	
	//Returns an array of all required devices
	public String[] getRequiredResources(){
		return this.requiredResources;
	}
	
	//Returns all resources accessed by this process as an arraylist
	public ArrayList<Resource> getAllResource(){
		return usedResources;
	}
	
	//Returns used port ids from the current process
	public String getUsedPorts(){
		String ports = "";
		for(Device dev : usedDevices){								//locate device in used devices
			ports = ports + dev.getPortID() + ", ";
		}
		for(Resource res : usedResources){							//locate resource in used resources
			ports = ports + res.getPortID() + ", ";
		}
		return ports;												//return the port ids
	}
	
	//Access critical section of process
	public void accessCriticalSection(){

		//flag[] is boolean array; and turn is an integer
		int turn;

		flag[pid] = true;
	    turn = 1;
	    while (flag[pid] == true && turn == 1)
	    {
	        // busy wait
	    }
	    
	    // critical section
	    // access a shared resource between processes
	    // end of critical section
	    
	    flag[pid] = false;
		
	}
	
	
}