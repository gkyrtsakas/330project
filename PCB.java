package project;

public class PCB {
	
	public Process process;
	public int pid;
	public String name;
	public int priority;
	public int submitTime;
	public int pCount;
	public int cpuTime;
	public int completeTime;
	public String state;
	public String schedule;
	//list of open files?
	
	public PCB(Process process) {
		this.process = process;
		this.name = process.getName();
		this.pid = process.getPid();
		this.priority = process.getPriority();
		this.submitTime = process.getSubmitTime();
		this.cpuTime = process.getCPUtime();
		this.completeTime = process.getCompleteTime();
		this.state = process.getState();
		this.schedule = process.getSchedule();
	}

}