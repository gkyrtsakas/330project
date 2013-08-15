public class Process{
	private int pid;
	private int priority;
	private int submitTime;
	private int waitTime;
	private int cpuTime;
	private int completeTime;
	private int sizeInRAM;
	private boolean deviceRequest;
	
	public Process (int pid, int prio, int sub, int size, boolean devReq){
		this.pid = pid;
		this.priority = prio;
		this.submitTime = sub;
		this.sizeInRAM = size;
		this.deviceRequest = devReq;
		
		this.waitTime = 0;
		this.cpuTime = 0;
		this.completeTime = 0;
	}
	
	public int getPid(){
		return pid;
	}
	
	public int getSubmitTime (){
		return submitTime;
	}
	
	public int getWaitTime (){
		return waitTime;
	}
	
	public int getCPUtime(){
		return cpuTime;
	}
	
	public int getCompleteTime (){
		return completeTime;
	}
	
	public void setCompleteTime (int time){
		this.completeTime = time;
	}
	
	public void incWait(){
		this.waitTime++;
	}
	
	public void incCPU(){
		this.cpuTime++;
	}
	
	
}