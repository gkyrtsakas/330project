import java.util.LinkedList; 
import java.util.*; 

public class QueueManager extends NonPreemptiveScheduler{
	private static LinkedList<Process> newQueue = new LinkedList<Process>();
	private static LinkedList<Process> readyQueue = new LinkedList<Process>();
	private static LinkedList<Process> runQueue = new LinkedList<Process>();
	private static LinkedList<Process> waitQueue = new LinkedList<Process>();
	private static LinkedList<Process> terminateQueue = new LinkedList<Process>();
	private static int cycleTime = 0;
	
	QueueManager(int schedulingType){	
		super(schedulingType);
	}
	
	public void addProcess(Process process){
		moveToNew(process, newQueue);
		moveFromNew();
	}
	
	public void update(){
		moveFromNew();
		moveFromRun();
		moveFromWait();
		moveFromReady();
	}
	
	public void cycleIncrement(){
		cycleTime ++;
	}
	
	public void moveFromNew(){
		while(newQueue.peek().getSubmitTime() <= cycleTime){
			readyQueue = moveFromNewToReady(newQueue.poll(), readyQueue);
		}
	}
	
	public void moveFromReady(){
		while(runQueue.isEmpty() && !readyQueue.isEmpty()){
			runQueue = moveFromReadyToRunning(readyQueue.poll(), runQueue);
			moveFromRun();
		}
	}
	
	public void moveFromRun(){
		if(runQueue.peek().getAllDevice().size() != runQueue.peek().getRequiredDevices().length 
				|| runQueue.peek().getAllResource().size() != runQueue.peek().getRequiredResources().length){
			runQueue = moveFromRunning(runQueue.poll(), waitQueue);
		}
		else{
			runQueue.element().setBurstCycle(runQueue.element().getBurstsCycle()-1);
			if(runQueue.peek().getBurstsCycle() <= 0){
				terminateQueue = moveFromRunning(runQueue.poll(), terminateQueue);
			}
		}
	}
	
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
	
	public String toString(){
		String returnString = "Current Cycle Time: " + cycleTime,
				newS, readyS, runS, waitS, termS;
		
		returnString = "|----New----|---Ready---|--Running--|-Waiting--|-Terminated-|";
		int newSize = newQueue.size(), readySize = readyQueue.size(), 
			runSize = runQueue.size(), waitSize = waitQueue.size(),
			terminateSize = terminateQueue.size(), max;
		
		List<Integer> findMax = Arrays.asList(newSize, readySize, runSize, waitSize, terminateSize);
		
		max = Collections.max(findMax);
		
		Process[] newArray = (Process[])newQueue.toArray(), readyArray = (Process[])readyQueue.toArray(),
				runArray = (Process[])runQueue.toArray(), waitArray = (Process[])waitQueue.toArray(),
				terminateArray = (Process[])terminateQueue.toArray();
		
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
				termS = newArray[i].getName();
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
}
