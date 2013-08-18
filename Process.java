package project;

import java.util.ArrayList;
import java.util.Vector;

public class Process implements Comparable<Process>, Cloneable{
	
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
	private Vector<Integer> burstsCycle;
	private String[] requiredDevices;				//list of devices required by the current process
	private ArrayList<Device> usedDevices;			//list of accessed devices in the current process
	private String[] requiredResources;				//list of resources required by the current process
	private ArrayList<Resource> usedResources;		//list of accessed resources in the current process
	private int waitTime;
	private int cpuTime;
	private int quantum;							//Quantum consumed
	private int current;
	private int completeTime;
	private int responseTime;
	private int order; 								//value to compare
	private String state;							//current state of the current process
	private double iorate;
	private int schedule;							//current scheduling algorithm on the process
	private static int maxPid = 1;
	private boolean[] flag;
	private boolean isPeriodic;

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
	public Process(int pid, String name, int priority, int submitTime, boolean periodic, Vector<Integer> burstsCycle, String requiredDevices, String requiredResources) {
		this.pid = pid;
		this.name = name;
		this.priority = priority;
		this.submitTime = submitTime;
		this.isPeriodic = periodic;
		this.burstsCycle = burstsCycle;
		this.responseTime = -1;
		int ioburst = 0;
		for (int i=0; i < burstsCycle.size(); i++) ioburst += burstsCycle.get(i);
		this.iorate = (double) ioburst / burstsCycle.size();
		this.state = PROCESS_STATE_NEW;
		this.requiredDevices = requiredDevices.split(" ");
		this.requiredResources = requiredResources.split(" ");
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
	
	//Returns True if process is periodic, False otherwise
	public boolean isPeriodic() {
		return this.isPeriodic;
	}
	
	public Vector<Integer> getBurstsCycle() {
		return this.burstsCycle;
	}
	
	//Returns the process current execution moment
	public int getCurrent() {
		return this.current;
	}
	
	//Returns current process burst moment
	public int getCurrentBurst() {
		if (this.isPeriodic) return this.current%this.burstsCycle.size();
		else return this.current;
	}
	
	//Increments the current process burst moment
	public void incCurrent() {
		this.current++;
	}
	
	//Returns the current process waiting time
	public int getWaitTime (){
		return this.waitTime;
	}
	
	//Increments the current waiting process time
	public void incWait(){
		this.waitTime++;
	}
	
	//Returns the current process CPU time
	public int getCPUtime(){
		return this.cpuTime;
	}
	
	//Increments the CPU time
	public void incCPU(){
		this.cpuTime++;
	}
	
	//Returns the current process completion time
	public int getCompleteTime (){
		return this.completeTime;
	}

	//Sets the current process completion time
	public void setCompleteTime (int time){
		this.completeTime = time;
	}
	
	//Returns the current process response time
	public int getResponseTime() {
		return this.responseTime;
	}

	//Sets the current process response time
	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}
	
	//Returns the current process quantum time
	public int getQuantum() {
		return this.quantum;
	}

	//Adds time to the current process quantum time
	public void addQexecuted(int val) {
		this.quantum += val;
	}

	//Sets time of the current process quantum time
	public void setQexecuted(int quantum) {
		this.quantum = quantum;
	}
	
	//Returns the current process order
	public int getOrder() {
		return this.order;
	}

	//Sets the current process order
	public void setOrder(int order) {
		this.order = order;
	}
	
	//Returns the current process state
	public String getState(){
		return this.state;
	}
	
	//Returns the current schedule on the process
	public int getSchedule(){
		return this.schedule;
	}
	
	//Sets a schedule on the current process
	public void setSchedule(int schedule){
		this.schedule = schedule;
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
	
	//Returns a specific device from the current process
	public Device getDevice(Device device){
		for(Device dev : usedDevices){								//locate device in used devices
			if(dev == device){
				return dev;											//return found device
			}
		}
		return null;												//else return null
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
	
	//Returns a specific resource from the current process
	public Resource getResource(Resource resource){
		for(Resource res : usedResources){							//locate resource in used resources
			if(res == resource){
				return res;											//return found resource
			}
		}
		return null;												//else return null
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
	
	/**
	 * Gets current burst duration, from current burst moment to different burst or process end's.
	 * When process burst cycle is periodic, current burst reaches process end's and first burst 
	 * is the same as last one, it adds first burst duration. 
	 * 
	 * @return Gets current burst duration
	 */
	public int getCurrentBurstDuration() {
		int cbduration = 0;
		int cb = burstsCycle.get(getCurrentBurst());
		int i = getCurrentBurst();
		while (i < burstsCycle.size() && burstsCycle.get(i) == cb) {
			cbduration++;
			i++;
		}
		if (isPeriodic && i >= burstsCycle.size() && burstsCycle.get(0) == cb) {  
			i = 0;
			while (i < getCurrentBurst() && burstsCycle.get(i) == cb) {
				cbduration++;
				i++;
			}
		}
		
		return cbduration;
	}
	
	//Returns True if the current process burst IO is current
	public boolean isCurrentIO() {
		return burstsCycle.get(getCurrentBurst()) == 1;
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

	public int compareTo(Process p) {
		if (this.order == p.getOrder()) return this.pid - p.getPid();
		else return this.order - p.getOrder();
	}
	
	//Creates a copy of the current process
	protected Process clone() {
	    Process clone = null;
		try {
			clone = (Process) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	    return clone;
	}
	
	
}