import java.util.*; 
import java.util.LinkedList; 

/* The QueueManager Class handles the management of the process state queues.
 * It extends the NonPreemptiveScheduler class in order to properly manage the processes within each queue.
 */
public class QueueManager extends NonPreemptiveScheduler{
	//The five default queues, one for each process state.
	private static LinkedList<Process> newQueue = new LinkedList<Process>();
	private static LinkedList<Process> readyQueue = new LinkedList<Process>();
	private static LinkedList<Process> runQueue = new LinkedList<Process>();
	private static LinkedList<Process> waitQueue = new LinkedList<Process>();
	private static LinkedList<Process> terminateQueue = new LinkedList<Process>();
	private static int cycleTime = 0;			//Keeps track of which cycle it is currently working in, in order to manage processes
	// Constructor
	public QueueManager(int schedulingType){	
		super(schedulingType);
	}
	//Adds a process to the new queue using NonPreemptiveScheduler 
	//class' moveToNew() function
	public void addProcess(Process process){
		moveToNew(process, newQueue);
		moveFromNew();
	}
	// Function called by the simulator to update all the queues
	public void update(){
		if(!newQueue.isEmpty())
			moveFromNew();
		if(!runQueue.isEmpty())
			moveFromRun();
		if(!waitQueue.isEmpty())
			moveFromWait();
		if(!readyQueue.isEmpty())
			moveFromReady();
	}
	// returns current cycle time
	public int getCycleTime()
	{
		return cycleTime;
	}
	// returns whether or not the processes have all been terminated
	public boolean isFinished()
	{
		if(newQueue.isEmpty() && readyQueue.isEmpty() && runQueue.isEmpty() && waitQueue.isEmpty())
		{
			return true;
		}
		
		return false;
	}
	// sets up all default devices to be used with the simulator
	public void setDefaultDeviceTable()
	{
		Device d1 = new Device("d1");
		Device d2 = new Device("d2");
		Device d3 = new Device("d3");
		Device d4 = new Device("d4");
		Device d5 = new Device("d5");
		Device d6 = new Device("d6");
		Device d7 = new Device("d7");
		Device d8 = new Device("d8");
		Device d9 = new Device("d9");
		Device d10 = new Device("d10");
	}
	// sets up all default resources to be used with the simulator
	public void setDefaultResourceTable()
	{
		Resource r1 = new Resource("r1");
		Resource r2 = new Resource("r2");
		Resource r3 = new Resource("r3");
		Resource r4 = new Resource("r4");
		Resource r5 = new Resource("r5");
		Resource r6 = new Resource("r6");
		Resource r7 = new Resource("r7");
		Resource r8 = new Resource("r8");
		Resource r9 = new Resource("r9");
		Resource r10 = new Resource("r10");
	}
	
	public void emptyQueues()
	{
		newQueue = new LinkedList<Process>();
		readyQueue = new LinkedList<Process>();
		runQueue = new LinkedList<Process>();
		waitQueue = new LinkedList<Process>();
		setTerminateQueue(new LinkedList<Process>());
		cycleTime = 0;
	}
	

	// Increments cycleTime to display relative time passed 
	// from when processes began to run

	public void cycleIncrement(){
		cycleTime ++;
	}
	// Moves items from the new queue into the ready queue if
	// the submit time is less than the cycle time
	public void moveFromNew(){
		if (!newQueue.isEmpty()){
			if(newQueue.peek().getSubmitTime() <= cycleTime){
				readyQueue = moveFromNewToReady(newQueue.poll(), readyQueue);
				moveFromNew();
			}
		}
	}
	// Moves processes from the ready queue to the running queue
	// from the front of the list
	@SuppressWarnings("unchecked")
	public void moveFromReady(){
		while(runQueue.isEmpty() && !readyQueue.isEmpty()){
			runQueue = moveFromReadyToRunning(readyQueue.poll(), runQueue);
			moveFromRun();
		}
	}
	// Moves processes from the run queue to:
	// 	1) waiting queue if it has no resources and is waiting on resources
	// 		and devices to be locked 
	// 	2) terminated queue if it is finished running
	// Otherwise it continues to decrement the burst time of the process
	// untill it is 0
	public void moveFromRun(){
		if (!runQueue.isEmpty()){
			if((runQueue.peek().getAllDevice().size() != runQueue.peek().getRequiredDevices().length
					|| runQueue.peek().getAllResource().size() != runQueue.peek().getRequiredResources().length)){
				runQueue.peek().setState("wait");
				waitQueue = moveFromRunning(runQueue.poll(), waitQueue);
			}
			else{
				runQueue.peek().setBurstCycle(runQueue.peek().getBurstsCycle()-1);
				if(runQueue.peek().getBurstsCycle() <= -1){
					runQueue.peek().removeAllDevices();
					runQueue.peek().removeAllResources();
					runQueue.peek().removeAllPages();
					runQueue.peek().setFinishTime(this.getCycleTime());
					setTerminateQueue(moveFromRunning(runQueue.poll(), getTerminateQueue()));
					
				}
			}
		}
		
	}
	// Moves processes from the wait queue to the ready queue when they are able
	// to lock all necessary resources and devices.
	public void moveFromWait(){
		Process p;
		if(!waitQueue.isEmpty()){
			int i = 0;
			while(i < waitQueue.size()){
				p = waitQueue.get(i);
				if(p.getResourcesAvailable() == true && p.getDevicesAvailable() == true){
					readyQueue = moveFromWaitingToReady(waitQueue.remove(i), readyQueue);
				}
				else{
					i++;
				}
			}
		}
	}
	// Returns a table of queues displaying which processes are present in which queues
	public String toString(){
		String returnString = "Current Cycle Time: " + cycleTime,
				newS, readyS, runS, waitS, termS;
		
		returnString += "\n|----New----|---Ready---|--Running--|--Waiting--|-Terminated|";
		int newSize = newQueue.size(), readySize = readyQueue.size(), 
			runSize = runQueue.size(), waitSize = waitQueue.size(),
			terminateSize = getTerminateQueue().size(), max;
		
		List<Integer> findMax = Arrays.asList(newSize, readySize, runSize, waitSize, terminateSize);
		
		max = Collections.max(findMax);
		
		Process[] newArray = newQueue.toArray(new Process[newQueue.size()]), readyArray = readyQueue.toArray(new Process[readyQueue.size()]),
				runArray = runQueue.toArray(new Process[runQueue.size()]), waitArray = waitQueue.toArray(new Process[waitQueue.size()]),
				terminateArray = getTerminateQueue().toArray(new Process[getTerminateQueue().size()]);
		
		for(int i=0; i < max; i++){
			if(i < newSize){
				newS = newArray[i].getName();
				for(int c = newS.length(); c < 11; c++){
					newS += ".";
				}
			}
			else{
				newS = "...........";
			}
			if(i < readySize){
				readyS = readyArray[i].getName();
				for(int c = readyS.length(); c < 11; c++){
					readyS += ".";
				}
			}
			else{
				readyS = "...........";
			}
			if(i < runSize){
				runS = runArray[i].getName();
				for(int c = runS.length(); c < 11; c++){
					runS += ".";
				}
			}
			else{
				runS = "...........";
			}
			if(i < waitSize){
				waitS = waitArray[i].getName();
				for(int c = waitS.length(); c < 11; c++){
					waitS += ".";
				}
			}
			else{
				waitS = "...........";
			}
			if(i < terminateSize){
				termS = terminateArray[i].getName();
				for(int c = termS.length(); c < 11; c++){
					termS += ".";
				}
			}
			else{
				termS = "...........";
			}
			
			
			returnString += "\n|" + newS + "|" + readyS + "|" 
							+ runS + "|" + waitS + "|" + termS + "|";
		}
		return returnString;
	}

	public static LinkedList<Process> getTerminateQueue() {
		return terminateQueue;
	}

	public static void setTerminateQueue(LinkedList<Process> terminateQueue) {
		QueueManager.terminateQueue = terminateQueue;
	}
}
