package process.scheduler;

import java.util.PriorityQueue;

public class SJF extends NonPreemptiveStrategy
{
	
	public void addProcessToQueue (Process p, PriorityQueue<Process> queue)
	{
		p.setOrder(p.getCurrentBurstDuration());
		queue.add(p);
	}
	
	public Process moveForward(PriorityQueue<Process> queue, Process runningProcess)
	{
		Process job = queue.peek();
		
		if (job != null && job.getCurrentBurstDuration() < runningProcess.getCurrentBurstDuration())
		{
			addProcess(runningProcess, queue);
			return queue.poll();
		}
		else
		{
			return runningProcess;
		}
	}

}